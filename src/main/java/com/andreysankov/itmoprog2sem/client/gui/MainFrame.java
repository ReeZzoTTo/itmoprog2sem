package com.andreysankov.itmoprog2sem.client.gui;

import com.andreysankov.itmoprog2sem.client.Client;
import com.andreysankov.itmoprog2sem.common.dto.CollectionResponse;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    private final Client client;
    private final String login;
    private final String password;

    private final LabWorkTableModel tableModel = new LabWorkTableModel();
    private final JTable table = new JTable(tableModel);

    public MainFrame(Client client, String login, String password) {
        this.client = client;
        this.login = login;
        this.password = password;

        setTitle("Лабораторная работа №8");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initLayout();
        loadCollection();
    }

    private void initLayout() {
        JLabel userLabel = new JLabel("Текущий пользователь: " + login);
        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(e -> loadCollection());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadCollection() {
        Request request = new Request(
                CommandType.GET_COLLECTION,
                null,
                null,
                login,
                password
        );

        Response response = client.sendRequest(request);

        if (response instanceof CollectionResponse collectionResponse) {
            if (collectionResponse.isSuccess()) {
                tableModel.setLabWorks(collectionResponse.getCollection());
            } else {
                showError(collectionResponse.getMessage());
            }
        } else {
            showError(response.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
    }
}