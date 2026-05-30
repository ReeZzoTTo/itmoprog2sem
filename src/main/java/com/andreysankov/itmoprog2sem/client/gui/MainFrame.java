package com.andreysankov.itmoprog2sem.client.gui;

import com.andreysankov.itmoprog2sem.client.Client;
import com.andreysankov.itmoprog2sem.common.dto.CollectionResponse;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.ArgumentId;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.Timer;

public class MainFrame extends JFrame {
    private final Client client;
    private final String login;
    private final String password;

    private boolean loadingCollection = false;
    private final Timer autoRefreshTimer = new Timer(3000, e -> loadCollection(false));

    private final LabWorkTableModel tableModel = new LabWorkTableModel();
    private final JTable table = new JTable(tableModel);

    private final VisualizationPanel visualizationPanel;

    private final JButton addButton = new JButton("Добавить");
    private final JButton editButton = new JButton("Редактировать");
    private final JButton deleteButton = new JButton("Удалить");

    private final JComboBox<String> filterColumnBox = new JComboBox<>(tableModel.getColumns());
    private final JTextField filterField = new JTextField(18);

    private final JComboBox<String> sortColumnBox = new JComboBox<>(tableModel.getColumns());
    private final JComboBox<String> sortDirectionBox = new JComboBox<>(new String[]{
            "По возрастанию",
            "По убыванию"
    });

    public MainFrame(Client client, String login, String password) {
        this.client = client;
        this.login = login;
        this.password = password;
        this.visualizationPanel = new VisualizationPanel(login, this::editLabWork);

        setTitle("Лаб.раб.№8 — Управление коллекцией лабораторных работ");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                autoRefreshTimer.stop();
            }
        });

        initLayout();
        loadCollection();
        autoRefreshTimer.start();
    }

    private void initLayout() {
        JLabel userLabel = new JLabel("Текущий пользователь: " + login);
        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(e -> loadCollection());

        JButton applyFilterButton = new JButton("Применить фильтр");
        applyFilterButton.addActionListener(e -> applyFilterAndSort());

        JButton resetFilterButton = new JButton("Сбросить");
        resetFilterButton.addActionListener(e -> {
            filterField.setText("");
            tableModel.setFilter(-1, "");
            visualizationPanel.setLabWorks(tableModel.getVisibleLabWorks());
        });

        JButton applySortButton = new JButton("Применить сортировку");
        applySortButton.addActionListener(e -> applyFilterAndSort());

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(refreshButton, BorderLayout.EAST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Фильтр по:"));
        controlPanel.add(filterColumnBox);
        controlPanel.add(filterField);
        controlPanel.add(applyFilterButton);
        controlPanel.add(resetFilterButton);
        controlPanel.add(new JLabel("Сортировать по:"));
        controlPanel.add(sortColumnBox);
        controlPanel.add(sortDirectionBox);
        controlPanel.add(applySortButton);

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(userPanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.SOUTH);

        table.setAutoCreateRowSorter(false);

        JScrollPane scrollPanel = new JScrollPane(table);

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            scrollPanel,
            visualizationPanel
        );

        splitPane.setResizeWeight(0.45);
        splitPane.setDividerLocation(250);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedLabWork();
                }
            }
        });

        addButton.addActionListener(e -> addLabWork());
        editButton.addActionListener(e -> editSelectedLabWork());
        deleteButton.addActionListener(e -> deleteSelectedLabWork());
    }

    private void applyFilterAndSort() {
        int filterColumn = filterColumnBox.getSelectedIndex();
        String filterText = filterField.getText();

        int sortColumn = sortColumnBox.getSelectedIndex();
        boolean ascending = sortDirectionBox.getSelectedIndex() == 0;

        tableModel.setFilter(filterColumn, filterText);
        tableModel.setSort(sortColumn, ascending);

        visualizationPanel.setLabWorks(tableModel.getVisibleLabWorks());
    }

    private void loadCollection() {
        loadCollection(true);
    }

    private void loadCollection(boolean showErrors) {
        if (loadingCollection) {
            return;
        }

        loadingCollection = true;

        new Thread(() -> {
            Request request = new Request(
                    CommandType.GET_COLLECTION,
                    null,
                    null,
                    login,
                    password
            );

            Response response = client.sendRequest(request);

            SwingUtilities.invokeLater(() -> {
                try {
                    if (response instanceof CollectionResponse collectionResponse) {
                        if (collectionResponse.isSuccess()) {
                            tableModel.setLabWorks(collectionResponse.getCollection());
                            visualizationPanel.setLabWorks(tableModel.getVisibleLabWorks());
                        } else if (showErrors) {
                            showError(collectionResponse.getMessage());
                        }
                    } else if (showErrors) {
                        showError(response.getMessage());
                    }
                } finally {
                    loadingCollection = false;
                }
            });
        }).start();
    }

    private void addLabWork() {
        LabWorkDialog dialog = new LabWorkDialog(this, null);
        dialog.setVisible(true);

        if (!dialog.isSaved()) {
            return;
        }

        Request request = new Request(
            CommandType.ADD,
            null,
            dialog.getLabWork(),
            login,
            password
        );

        sendChangingRequest(request);
    }

    private void editSelectedLabWork() {
        LabWork selected = getSelectedLabWork();

        if (selected == null) {
            showError("Выберите объект для редактирования");
            return;
        }

        editLabWork(selected);
    }

    private void editLabWork(LabWork selected) {
        if (!login.equals(selected.getOwnerLogin())) {
            showError("Можно редактировать только свои объекты");
            return;
        }

        LabWorkDialog dialog = new LabWorkDialog(this, selected);
        dialog.setVisible(true);

        if (!dialog.isSaved()) {
            return;
        }

        Request request = new Request(
                CommandType.UPDATE_ID,
                new ArgumentId(String.valueOf(selected.getId())),
                dialog.getLabWork(),
                login,
                password
        );

        sendChangingRequest(request);
    }

    private void deleteSelectedLabWork() {
        LabWork selected = getSelectedLabWork();

        if (selected == null) {
            showError("Выберите объект для удаления");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
            this,
            "Удалить объект с ID = " + selected.getId() + "?",
            "Подтверждение",
            JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        Request request = new Request(
            CommandType.REMOVE_BY_ID,
            new ArgumentId(String.valueOf(selected.getId())),
            null,
            login,
            password
        );

        sendChangingRequest(request);
    }

    private LabWork getSelectedLabWork() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            return null;
        }

        return tableModel.getLabWorkAt(selectedRow);
    }

    private void sendChangingRequest(Request request) {
        Response response = client.sendRequest(request);

        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Успешно",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadCollection();
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