package jetbrains.zeppelin.service

import java.util.concurrent.Executors

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import jetbrains.zeppelin.api._
import jetbrains.zeppelin.api.websocket.{OutputHandler, OutputResult}
import jetbrains.zeppelin.utils.ZeppelinLogger

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.Try

/**
  * Main class that implement logic of the actions for the communication with Zeppelin
  */
class ZeppelinActionService(project: Project, address: String, port: Int, user: Option[User]) {
  var zeppelinService: ZeppelinAPIService = ZeppelinAPIService(address, port, user)

  /**
    * Method that close all connections and free resources
    */
  def destroy(): Unit = {
    zeppelinService.close()
  }

  /**
    * Get interpreter settings by interpreter id
    *
    * @param interpreterId - an id of an interpreter
    * @return an interpreter model
    */
  def getInterpreterById(interpreterId: String): Interpreter = {
    zeppelinService.interpreterById(interpreterId)
  }

  /**
    * Get interpreter settings for a notebook by name
    *
    * @param interpreterName - a name of an interpreter
    * @return an interpreter model
    */
  def getInterpreterByName(interpreterName: String): Interpreter = {
    val interpreter = interpreterList().find(_.name == interpreterName.split(" ").head).get
    interpreter
  }

  /**
    * Get a list of available interpreters for the notebook
    *
    * @return the list with interpreters
    */
  def interpreterList(): List[Interpreter] = {
    if (!checkPreconditions()) return List()

    val allInterpreters = zeppelinService.allInterpreters
    val notebook = zeppelinService.getOrCreateNotebook(notebookName)
    val defaultInterpreter = zeppelinService.defaultInterpreter(notebook.id)
    defaultInterpreter +: allInterpreters.filter(_.id != defaultInterpreter.id)
  }

  /**
    * Run code on the Zeppelin server
    *
    * @param code - code, that be executed
    */
  def runCode(code: String): Unit = {
    if (!checkPreconditions()) return

    if (code.isEmpty) {
      ZeppelinLogger.printMessage("The selected text is empty, please select a piece of code")
      return
    }

    ZeppelinLogger.printMessage(s"Run paragraph with text: $code")
    val handler = new OutputHandler {
      override def onError(): Unit = {
        ZeppelinLogger.printError("Paragraph Run Error")
      }

      override def handle(result: OutputResult, isAppend: Boolean): Unit = {
        if (result.data.isEmpty) return
        ZeppelinLogger.printMessage(result.data)
      }

      override def onSuccess(executionResults: ExecutionResults = ExecutionResults()): Unit = {
        executionResults.msg.foreach(it => {
          it.resultType match {
            case "TABLE" => ZeppelinLogger.printMessage(it.data)
            case _ => Unit
          }
        })
        ZeppelinLogger.printMessage("Paragraph is completed")
      }
    }
    zeppelinService.runCode(code, handler, notebookName)
  }

  /**
    * Set the selected interpreter as a default interpreter for the notebook
    *
    * @param interpreterName - an interpreter name
    */
  def setDefaultInterpreter(interpreterName: String): Unit = {
    val interpreter: Interpreter = getInterpreterByName(interpreterName)
    val notebook = zeppelinService.getOrCreateNotebook(notebookName)
    zeppelinService.setDefaultInterpreter(notebook.id, interpreter.id)
  }

  /**
    * Upload new interpreter settings
    *
    * @param interpreter - a model of an interpreter
    */
  def updateInterpreterSettings(interpreter: Interpreter): Unit = {
    if (!checkPreconditions()) return

    val oldInterpreterSettings = getInterpreterById(interpreter.id)
    val oldDependencies = oldInterpreterSettings.dependencies
    val newDependencies = interpreter.dependencies

    val removedDependencies = oldDependencies.filter(it => !newDependencies.contains(it))
    val addedDependencies = newDependencies.filter(it => !oldDependencies.contains(it))

    if (removedDependencies.nonEmpty) {
      ZeppelinLogger.printMessage("The next dependencies will be removed:")
      removedDependencies.foreach(it => ZeppelinLogger.printMessage(it.groupArtifactVersion))
    }

    if (addedDependencies.nonEmpty) {
      ZeppelinLogger.printMessage("The next dependencies will be added:")
      addedDependencies.foreach(it => ZeppelinLogger.printMessage(it.groupArtifactVersion))
    }


    // single threaded execution context
    implicit val context: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

    val f = Future {
      zeppelinService.updateInterpreterSetting(interpreter)
    }

    f.onComplete {
      result => {
        if (result.isSuccess) {
          ZeppelinLogger
            .printSuccess("Interpreter settings were updated." +
              s" Removed: ${removedDependencies.size}." +
              s" Added: ${addedDependencies.size}.")
        }
        if (result.isFailure) {
          ZeppelinLogger.printError("The error during the updating")
        }
      }
    }
  }

  /**
    * Test the connection and try to connect if the server is disconnected
    *
    * @return the server is connected
    */
  private def checkConnection: Boolean = {
    if (zeppelinService.isConnected) return true
    try {
      zeppelinService.close()
      zeppelinService = ZeppelinAPIService(address, port, user)
      zeppelinService.connect(false)
    }
    catch {
      case _: ZeppelinConnectionException => {
        ZeppelinLogger
          .printError(s"Connection error. Check that $address:$port is available")
      }
      case _: ZeppelinLoginException => ZeppelinLogger.printError(s"Authentication error. Check login and password")
    }
    zeppelinService.isConnected
  }

  /**
    * Check that an user open any file for the notebook
    *
    * @return a file is open or not
    */
  private def checkOpenFile: Boolean = {
    try {
      notebookName
      true
    }
    catch {
      case _: NoSelectedFilesException => {
        ZeppelinLogger.printError("Please, open any file, a notebook will be created for a specific file")
        false
      }
    }
  }

  /**
    * Check preconditions before the performing a request
    *
    * @return a request can be performed or not
    */
  private def checkPreconditions(): Boolean = {
    checkConnection && checkOpenFile
  }

  /**
    * Get a notebook name for the current file
    *
    * @return a name of a file
    */
  private def notebookName: String = {
    val prefix = s"IdeaRemoteRunPlugin/${project.getName}/"
    val name = Try(FileEditorManager.getInstance(project).getSelectedEditor.getFile.getName)
      .getOrElse(throw new NoSelectedFilesException())
    prefix + name
  }
}

object ZeppelinActionService {
  def apply(project: Project, address: String, port: Int, user: Option[User]): ZeppelinActionService = {
    new ZeppelinActionService(project, address, port, user)
  }
}