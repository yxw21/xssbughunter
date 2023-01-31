import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.persistence.PersistedObject;
import models.Login;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Timer;

public class XSSBugHunter implements BurpExtension {

    private MontoyaApi api;
    private PersistedObject persistedObject;
    private final JTextField userText = new JTextField(20);
    private final JPasswordField passwordText = new JPasswordField(20);
    private final JTextField replaceText = new JTextField(20);
    private final JLabel errorLabel = new JLabel("");

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        this.persistedObject = api.persistence().extensionData();
        api.extension().setName("XSSBugHunter");
        api.proxy().registerRequestHandler(new RequestHandler(api, persistedObject));
        api.userInterface().registerSuiteTab("XSS", UIComponent());
        java.util.Timer tokenTimer = new Timer();
        tokenTimer.schedule(new TokenTimerTask(api, persistedObject), 0, 60 * 1000);
    }

    private Component UIComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        panel.add(userLabel);

        userText.setBounds(120, 20, 168, 25);
        userText.setText(persistedObject.getString("username"));
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText.setBounds(120, 50, 168, 25);
        passwordText.setText(persistedObject.getString("password"));
        panel.add(passwordText);

        JLabel replaceLabel = new JLabel("replace:");
        replaceLabel.setBounds(20, 80, 80, 25);
        panel.add(replaceLabel);

        replaceText.setBounds(120, 80, 168, 25);
        replaceText.setText(persistedObject.getString("replace"));
        panel.add(replaceText);

        errorLabel.setBounds(20, 110, 300, 25);
        panel.add(errorLabel);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(20, 140, 80, 25);
        loginButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = userText.getText();
                    String password = new String(passwordText.getPassword());
                    Login login = Utils.login(username, password);
                    if (login.isSuccess()) {
                        persistedObject.setString("username", username);
                        persistedObject.setString("password", password);
                        persistedObject.setString("replace", replaceText.getText());
                        persistedObject.setString("token", login.getToken());
                        persistedObject.setString("domain", login.getUser().getDomain());
                        errorLabel.setForeground(Color.green);
                    } else {
                        errorLabel.setForeground(Color.red);
                    }
                    errorLabel.setText(login.getMessage());
                } catch (Exception ec) {
                    errorLabel.setText("login failed");
                    api.logging().logToError(ec.getMessage());
                }
            }
        });
        panel.add(loginButton);
        return panel;
    }
}
