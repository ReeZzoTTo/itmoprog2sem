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
import java.util.Locale;

import javax.swing.Timer;

public class MainFrame extends JFrame {
    private final Client client;
    private final String login;
    private final String password;

    private final LocalizationManager localization = new LocalizationManager(Locale.of("ru", "RU"));

    private boolean loadingCollection = false;
    private final Timer autoRefreshTimer = new Timer(3000, e -> loadCollection(false));

    private final LabWorkTableModel tableModel = new LabWorkTableModel();
    private final JTable table = new JTable(tableModel);

    private final VisualizationPanel visualizationPanel;

    private final JButton addButton = new JButton();
    private final JButton editButton = new JButton();
    private final JButton deleteButton = new JButton();

    private final JComboBox<String> filterColumnBox = new JComboBox<>(tableModel.getColumns());
    private final JTextField filterField = new JTextField(18);

    private final JComboBox<String> sortColumnBox = new JComboBox<>(tableModel.getColumns());
    private final JComboBox<String> sortDirectionBox = new JComboBox<>(new String[]{
            "По возрастанию",
            "По убыванию"
    });

    private final JLabel userLabel = new JLabel();

    private final JButton refreshButton = new JButton();
    private final JButton applyFilterButton = new JButton();
    private final JButton resetFilterButton = new JButton();
    private final JButton applySortButton = new JButton();

    private final JLabel filterLabel = new JLabel();
    private final JLabel sortLabel = new JLabel();
    private final JLabel languageLabel = new JLabel();

    private final JComboBox<String> languageBox = new JComboBox<>(new String[]{
            "Русский",
            "Eesti",
            "Shqip",
            "English (India)"
    });

    public MainFrame(Client client, String login, String password) {
        this.client = client;
        this.login = login;
        this.password = password;
        this.visualizationPanel = new VisualizationPanel(login, this::editLabWork, localization);

        tableModel.setLocalization(localization);
        tableModel.setLocale(localization.getLocale());

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
        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        refreshButton.addActionListener(e -> loadCollection());

        applyFilterButton.addActionListener(e -> applyFilterAndSort());

        resetFilterButton.addActionListener(e -> {
            filterField.setText("");
            tableModel.setFilter(-1, "");
            visualizationPanel.setLabWorks(tableModel.getVisibleLabWorks());
        });

        applySortButton.addActionListener(e -> applyFilterAndSort());

        languageBox.addActionListener(e -> changeLanguage());

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(refreshButton, BorderLayout.EAST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        controlPanel.add(filterLabel);
        controlPanel.add(filterColumnBox);
        controlPanel.add(filterField);
        controlPanel.add(applyFilterButton);
        controlPanel.add(resetFilterButton);

        controlPanel.add(sortLabel);
        controlPanel.add(sortColumnBox);
        controlPanel.add(sortDirectionBox);
        controlPanel.add(applySortButton);

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

        controlPanel.add(languageLabel);
        controlPanel.add(languageBox);

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

        updateComboBoxItems();
        updateTexts();
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

    private void changeLanguage() {
        int index = languageBox.getSelectedIndex();

        Locale newLocale = switch (index) {
            case 0 -> Locale.of("ru", "RU");
            case 1 -> Locale.of("et", "EE");
            case 2 -> Locale.of("sq", "AL");
            case 3 -> Locale.of("en", "IN");
            default -> Locale.of("ru", "RU");
        };

        localization.setLocale(newLocale);
        tableModel.setLocale(newLocale);
        tableModel.setLocalization(localization);

        updateComboBoxItems();
        updateTexts();

        visualizationPanel.repaint();
    }

    private void updateTexts() {
        setTitle(localization.get("app.title"));

        userLabel.setText(localization.get("main.current_user") + " " + login);
        refreshButton.setText(localization.get("main.refresh"));

        filterLabel.setText(localization.get("main.filter_by"));
        applyFilterButton.setText(localization.get("main.apply_filter"));
        resetFilterButton.setText(localization.get("main.reset"));

        sortLabel.setText(localization.get("main.sort_by"));
        applySortButton.setText(localization.get("main.apply_sort"));

        addButton.setText(localization.get("main.add"));
        editButton.setText(localization.get("main.edit"));
        deleteButton.setText(localization.get("main.delete"));

        languageLabel.setText(localization.get("main.language"));
    }

    private void updateComboBoxItems() {
        int filterIndex = filterColumnBox.getSelectedIndex();
        int sortIndex = sortColumnBox.getSelectedIndex();
        int sortDirectionIndex = sortDirectionBox.getSelectedIndex();

        filterColumnBox.removeAllItems();
        sortColumnBox.removeAllItems();

        for (String column : tableModel.getColumns()) {
            filterColumnBox.addItem(column);
            sortColumnBox.addItem(column);
        }

        sortDirectionBox.removeAllItems();
        sortDirectionBox.addItem(localization.get("main.sort_asc"));
        sortDirectionBox.addItem(localization.get("main.sort_desc"));

        if (filterIndex >= 0 && filterIndex < filterColumnBox.getItemCount()) {
            filterColumnBox.setSelectedIndex(filterIndex);
        }

        if (sortIndex >= 0 && sortIndex < sortColumnBox.getItemCount()) {
            sortColumnBox.setSelectedIndex(sortIndex);
        }

        if (sortDirectionIndex >= 0 && sortDirectionIndex < sortDirectionBox.getItemCount()) {
            sortDirectionBox.setSelectedIndex(sortDirectionIndex);
        }
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
        LabWorkDialog dialog = new LabWorkDialog(this, null, localization);
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
            showError(localization.get("message.choose_edit"));
            return;
        }

        editLabWork(selected);
    }

    private void editLabWork(LabWork selected) {
        if (!login.equals(selected.getOwnerLogin())) {
            showError(localization.get("message.only_own_edit"));
            return;
        }

        LabWorkDialog dialog = new LabWorkDialog(this, selected, localization);
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
            showError(localization.get("message.choose_delete"));
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
            this,
            localization.get("message.confirm_delete") + " " + selected.getId() + "?",
            localization.get("message.confirm_title"),
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
                    localization.get("message.success"),
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
                localization.get("message.error"),
                JOptionPane.ERROR_MESSAGE
        );
    }
}