package jetbrains.zeppelin.toolwindow.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.{AnActionEvent, Presentation}
import com.intellij.openapi.project.DumbAwareAction
import jetbrains.zeppelin.components.ZeppelinConnection

/**
  * Set a new default interpreter for the Zeppelin
  */
class SetDefaultInterpretersAction extends DumbAwareAction {
  val templatePresentation: Presentation = getTemplatePresentation
  templatePresentation.setIcon(AllIcons.Actions.Get)
  templatePresentation.setText("Set interpreter as a default")

  override def actionPerformed(event: AnActionEvent): Unit = {
    val connection = ZeppelinConnection.connectionFor(event.getProject)
    val interpretersView = connection.interpretersView
    val interpreterName = interpretersView.getSelectedValue
    val service = connection.service

    service.setDefaultInterpreter(event.getProject.getName, interpreterName)
    connection.updateInterpreterList(event.getProject.getName)
  }
}