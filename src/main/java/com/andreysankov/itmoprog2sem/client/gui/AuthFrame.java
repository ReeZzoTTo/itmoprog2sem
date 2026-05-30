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

public class AuthFrame extends JFrame {
    private final Client client;

    private final JTextField loginField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public AuthFrame(String host, int port) {
        this.client = new Client(host, port, 3000);

        setTitle("Авторизация");
        setSize(380, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initLayout();
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formPanel.add(new JLabel("Логин:"));
        formPanel.add(loginField);

        formPanel.add(new JLabel("Пароль:"));
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Войти");
        JButton registerButton = new JButton("Зарегистрироваться");

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
                    "Введите логин и пароль",
                    "Ошибка",
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
                    "Успешно",
                    JOptionPane.INFORMATION_MESSAGE
            );

            MainFrame mainFrame = new MainFrame(client, login, password);
            mainFrame.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
