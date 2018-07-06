package jetbrains.zeppelin.toolwindow

import com.intellij.openapi.actionSystem.{ActionManager, DefaultActionGroup}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.intellij.ui.content.{Content, ContentFactory}
import jetbrains.zeppelin.components.ZeppelinConnection
import jetbrains.zeppelin.toolwindow.actions.{RefreshInterpretersAction, RunCodeAction, SetDefaultInterpretersAction, UpdateJarOnZeppelin}
import jetbrains.zeppelin.utils.ZeppelinLogger

/**
  * Factory that creates a Zeppelin tool window
  */
class ZeppelinToolWindowFactory extends ToolWindowFactory {
  override def init(toolWindow: ToolWindow): Unit = {
    toolWindow.setStripeTitle("Zeppelin")
  }

  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {
    toolWindow.getContentManager.addContent(createLogPanel(project))
    toolWindow.getContentManager.addContent(createInterpretersPanel(project))
  }

  private def createLogPanel(project: Project): Content = {
    val panel = new SimpleToolWindowPanel(false, true)
    val console = new ZeppelinConsole(project)
    ZeppelinLogger.initOutput(console)
    panel.setContent(console)
    val toolbar = createLogToolbar(project, console)
    panel.setToolbar(toolbar.getComponent)
    val content = ContentFactory.SERVICE.getInstance.createContent(panel, "Log", true)
    Disposer.register(project, console)
    content
  }

  private def createLogToolbar(project: Project, console: ZeppelinConsole) = {
    val group = new DefaultActionGroup
    group.add(new ClearLogActionConsole(console))
    group.add(new RunCodeAction(project))
    group.add(new UpdateJarOnZeppelin())
    val toolbar = ActionManager.getInstance.createActionToolbar("left", group, false)
    toolbar.setTargetComponent(console.getComponent)
    toolbar
  }

  private def createInterpretersPanel(project: Project): Content = {
    val panel = new SimpleToolWindowPanel(false, true)

    val interpretersView = ZeppelinConnection.connectionFor(project).interpretersView
    interpretersView.updateInterpretersList(List("Please refresh the list..."))
    panel.setContent(interpretersView)
    val toolbar = createInterpretersToolbar(project, interpretersView)
    panel.setToolbar(toolbar.getComponent)
    val content = ContentFactory.SERVICE.getInstance.createContent(panel, "Interpreters", true)
    Disposer.register(project, interpretersView)
    content
  }

  private def createInterpretersToolbar(project: Project, interpreters: InterpretersView) = {
    val group = new DefaultActionGroup
    group.add(new RefreshInterpretersAction())
    group.add(new SetDefaultInterpretersAction())
    val toolbar = ActionManager.getInstance.createActionToolbar("left", group, false)
    toolbar.setTargetComponent(interpreters.getComponent)
    toolbar
  }
}