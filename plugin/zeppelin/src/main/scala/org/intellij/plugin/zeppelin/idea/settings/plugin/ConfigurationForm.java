package org.intellij.plugin.zeppelin.idea.settings.plugin;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.intellij.plugin.zeppelin.components.ZeppelinComponent;
import org.intellij.plugin.zeppelin.components.ZeppelinComponent$;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ConfigurationForm implements SearchableConfigurable {
    private final Project myProject;
    private JPanel settingForm;
    private JTextField addressField;
    private JTextField portField;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JCheckBox anonymousCheckBox;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField sparkVersionTextField;
    private JTextField defaultNotebookField;
    private boolean isAnonymous;

    public ConfigurationForm(@NotNull Project project) {
        myProject = project;
        setDefaultValues(project);
    }

    @Override
    public void apply() {
        if (!settingForm.isVisible()) {
            return;
        }

        final ZeppelinComponent connection = ZeppelinComponent$.MODULE$.connectionFor(myProject);

        ZeppelinSettings newZeppelinSettings = getZeppelinSettingsFromForm();
        if (isModified()) {
            connection.updateSettings(newZeppelinSettings);
        }
    }

    @Override
    public JComponent createComponent() {
        return settingForm;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Zeppelin Notebook";
    }

    @Override
    public String getHelpTopic() {
        return "";
    }

    @NotNull
    @Override
    public String getId() {
        return "ZeppelinConfigurable";
    }

    @Override
    public boolean isModified() {
        final ZeppelinComponent connection = ZeppelinComponent$.MODULE$.connectionFor(myProject);

        final ZeppelinSettings zeppelinSettings = connection.getZeppelinSettings();
        final ZeppelinSettings newZeppelinSettings = getZeppelinSettingsFromForm();

        return !zeppelinSettings.equals(newZeppelinSettings);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        settingForm = new JPanel();
        settingForm.setLayout(new GridLayoutManager(8, 2, new Insets(0, 10, 0, 10), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Uri:");
        settingForm.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Port:");
        settingForm.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameLabel = new JLabel();
        usernameLabel.setText("Username:");
        settingForm.add(usernameLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portField = new JTextField();
        portField.setText("8080");
        settingForm.add(portField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        settingForm.add(passwordLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setText("password1");
        settingForm.add(passwordField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        usernameField = new JTextField();
        usernameField.setText("admin");
        settingForm.add(usernameField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addressField = new JTextField();
        addressField.setText("localhost");
        settingForm.add(addressField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        settingForm.add(spacer1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Authentication:");
        settingForm.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        anonymousCheckBox = new JCheckBox();
        anonymousCheckBox.setEnabled(true);
        anonymousCheckBox.setSelected(false);
        anonymousCheckBox.setText("Anonymous");
        settingForm.add(anonymousCheckBox, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        settingForm.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Spark version:");
        panel1.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sparkVersionTextField = new JTextField();
        panel1.add(sparkVersionTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel1.add(separator1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        settingForm.add(panel2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Default notebook:");
        panel2.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        defaultNotebookField = new JTextField();
        panel2.add(defaultNotebookField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JSeparator separator2 = new JSeparator();
        panel2.add(separator2, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return settingForm;
    }

    @NotNull
    private ZeppelinSettings getZeppelinSettingsFromForm() {
        ZeppelinSettings newZeppelinSettings = new ZeppelinSettings();
        newZeppelinSettings.setAddress(addressField.getText());
        newZeppelinSettings.setPort(Integer.valueOf(portField.getText()));
        newZeppelinSettings.setIsAnonymous(anonymousCheckBox.isSelected());
        String login = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        newZeppelinSettings.setCredentials(login, password);
        newZeppelinSettings.setDefaultNotebookName(String.valueOf(defaultNotebookField.getText()));
        return newZeppelinSettings;
    }

    private void setDefaultValues(@NotNull Project project) {
        ZeppelinComponent connection = ZeppelinComponent$.MODULE$.connectionFor(project);
        ZeppelinSettings zeppelinSettings = connection.getZeppelinSettings();
        passwordField.setText(zeppelinSettings.password());
        usernameField.setText(zeppelinSettings.login());
        portField.setText(String.valueOf(zeppelinSettings.port()));
        addressField.setText(zeppelinSettings.address());

        sparkVersionTextField.setText(zeppelinSettings.sparkVersion());
        defaultNotebookField.setText(zeppelinSettings.defaultNotebookName());

        isAnonymous = zeppelinSettings.isAnonymous();
        anonymousCheckBox.setSelected(isAnonymous);
        setShowAuthPanel(!isAnonymous);
        anonymousCheckBox.addItemListener(e -> {
            isAnonymous = !isAnonymous;
            setShowAuthPanel(!isAnonymous);
        });
    }

    private void setShowAuthPanel(boolean isAuth) {
        passwordField.setVisible(isAuth);
        usernameField.setVisible(isAuth);
        usernameLabel.setVisible(isAuth);
        passwordLabel.setVisible(isAuth);
    }
}