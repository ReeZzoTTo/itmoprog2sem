package com.andreysankov.itmoprog2sem.client.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.BorderLayout;

import com.andreysankov.itmoprog2sem.client.Client;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import java.util.Locale;
public class AuthFrame extends JFrame {
    private final Client client;
    private final LocalizationManager localization = new LocalizationManager(Locale.of("ru", "RU"));

    private final JTextField loginField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public AuthFrame(String host, int port) {
        this.client = new Client(host, port, 3000);

        setTitle(localization.get("auth.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initLayout();

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formPanel.add(new JLabel(localization.get("auth.login")));
        formPanel.add(loginField);

        formPanel.add(new JLabel(localization.get("auth.password")));
        formPanel.add(passwordField);

        JButton loginButton = new JButton(localization.get("auth.sign_in"));
        JButton registerButton = new JButton(localization.get("auth.register"));

        loginButton.addActionListener(e -> authenticate(CommandType.LOGIN));
        registerButton.addActionListener(e -> authenticate(CommandType.REGISTER));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

     private void authenticate(CommandType commandType) {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (login.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                localization.get("auth.empty_fields"),
                localization.get("message.error"),
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Request request = new Request(
                commandType,
                null,
                null,
                login,
                password
        );

        Response response = client.sendRequest(request);

        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    localization.get("message.success"),
                    JOptionPane.INFORMATION_MESSAGE
            );

            MainFrame mainFrame = new MainFrame(client, login, password);
            mainFrame.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    localization.get("message.error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
