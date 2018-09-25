package org.intellij.plugin.zeppelin.scala.worksheet.settings;

import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.intellij.plugin.zeppelin.idea.settings.notebook.NotebookBrowserDialog;
import org.intellij.plugin.zeppelin.models.Notebook;
import scala.Option;
import scala.Tuple2;

import javax.swing.*;
import java.awt.*;

public class ZeppelinWorksheetSettingsForm extends JDialog {
    private JPanel contentPane;
    private JPanel syncPanel;
    private JCheckBox linkToNotebookCheckBox;
    private JTextField selectedNotebookField;
    private JButton browseNotebooksButton;
    private Notebook notebook;

    public ZeppelinWorksheetSettingsForm(Project project) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        selectedNotebookField.setEditable(false);
        linkToNotebookCheckBox.addActionListener(e -> {
            setSyncNotebook(linkToNotebookCheckBox.isSelected());
        });
        browseNotebooksButton.addActionListener(e -> {
            NotebookBrowserDialog notebookBrowserDialog = new NotebookBrowserDialog(project, notebook);
            Tuple2<Object, Option<Notebook>> result = notebookBrowserDialog.openAndGetResult();
            Boolean isSelected = (Boolean) result._1;
            if (!isSelected) return;
            Option<Notebook> notebookOption = result._2;

            Notebook notebook = notebookOption.isDefined() ? notebookOption.get() : null;
            setNotebook(notebook);
        });
    }

    @Override
    public JPanel getContentPane() {
        return contentPane;
    }

    public Notebook getNotebook() {
        return notebook;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
        selectedNotebookField.setText(notebook != null ? notebook.name() : "");
    }

    public void setSyncNotebook(boolean value) {
        linkToNotebookCheckBox.setSelected(value);
        syncPanel.setVisible(value);
        if (!value) setNotebook(null);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 7, new Insets(10, 10, 10, 10), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Synchronization:");
        contentPane.add(label1, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        syncPanel = new JPanel();
        syncPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(syncPanel, new GridConstraints(1, 1, 2, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectedNotebookField = new JTextField();
        syncPanel.add(selectedNotebookField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseNotebooksButton = new JButton();
        browseNotebooksButton.setText("Select");
        syncPanel.add(browseNotebooksButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Notebook:");
        syncPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        linkToNotebookCheckBox = new JCheckBox();
        linkToNotebookCheckBox.setText("Link to notebook");
        contentPane.add(linkToNotebookCheckBox, new GridConstraints(0, 3, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}