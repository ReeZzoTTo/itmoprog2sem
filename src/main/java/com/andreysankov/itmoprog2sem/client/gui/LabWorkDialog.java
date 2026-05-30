package com.andreysankov.itmoprog2sem.client.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.andreysankov.itmoprog2sem.common.models.Coordinates;
import com.andreysankov.itmoprog2sem.common.models.Difficulty;
import com.andreysankov.itmoprog2sem.common.models.Discipline;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class LabWorkDialog extends JDialog {
     private boolean saved = false;

    private final JTextField nameField = new JTextField();
    private final JTextField xField = new JTextField();
    private final JTextField yField = new JTextField();
    private final JTextField minimalPointField = new JTextField();
    private final JTextField personalQualitiesMaximumField = new JTextField();

    private final JComboBox<Difficulty> difficultyBox = new JComboBox<>(Difficulty.values());

    private final JTextField disciplineNameField = new JTextField();
    private final JTextField lectureHoursField = new JTextField();
    private final JTextField labsCountField = new JTextField();

    private LabWork result;

    public LabWorkDialog(Frame owner, LabWork existingLabWork) {
        super(owner, true);

        setTitle(existingLabWork == null ? "Добавить объект" : "Редактировать объект");
        initLayout();

        if (existingLabWork != null) {
            fillFields(existingLabWork);
        }

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(owner);
    }

    private void initLayout() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        formPanel.add(new JLabel("Название:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("X:"));
        formPanel.add(xField);

        formPanel.add(new JLabel("Y:"));
        formPanel.add(yField);

        formPanel.add(new JLabel("Minimal point:"));
        formPanel.add(minimalPointField);

        formPanel.add(new JLabel("Personal qualities maximum:"));
        formPanel.add(personalQualitiesMaximumField);

        formPanel.add(new JLabel("Difficulty:"));
        formPanel.add(difficultyBox);

        formPanel.add(new JLabel("Discipline name:"));
        formPanel.add(disciplineNameField);

        formPanel.add(new JLabel("Lecture hours:"));
        formPanel.add(lectureHoursField);

        formPanel.add(new JLabel("Labs count:"));
        formPanel.add(labsCountField);

        JButton saveButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Отмена");

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void fillFields(LabWork labWork) {
        nameField.setText(labWork.getName());

        if (labWork.getCoordinates() != null) {
            xField.setText(String.valueOf(labWork.getCoordinates().getX()));
            yField.setText(String.valueOf(labWork.getCoordinates().getY()));
        }

        minimalPointField.setText(String.valueOf(labWork.getMinimalPoint()));

        if (labWork.getPersonalQualitiesMaximum() != null) {
            personalQualitiesMaximumField.setText(String.valueOf(labWork.getPersonalQualitiesMaximum()));
        }

        difficultyBox.setSelectedItem(labWork.getDifficulty());

        if (labWork.getDiscipline() != null) {
            disciplineNameField.setText(labWork.getDiscipline().getName());
            lectureHoursField.setText(String.valueOf(labWork.getDiscipline().getLectureHours()));
            labsCountField.setText(String.valueOf(labWork.getDiscipline().getLabsCount()));
        }
    }

    private void save() {
        try {
            String name = nameField.getText().trim();

            if (name.isBlank()) {
                throw new IllegalArgumentException("Название не может быть пустым");
            }

            Long x = Long.parseLong(xField.getText().trim());
            int y = Integer.parseInt(yField.getText().trim());

            int minimalPoint = Integer.parseInt(minimalPointField.getText().trim());

            if (minimalPoint <= 0) {
                throw new IllegalArgumentException("Minimal point должен быть больше 0");
            }

            Double personalQualitiesMaximum = null;

            if (!personalQualitiesMaximumField.getText().trim().isBlank()) {
                personalQualitiesMaximum = Double.parseDouble(
                        personalQualitiesMaximumField.getText().trim()
                );

                if (personalQualitiesMaximum <= 0) {
                    throw new IllegalArgumentException("Personal qualities maximum должен быть больше 0");
                }
            }

            Difficulty difficulty = (Difficulty) difficultyBox.getSelectedItem();

            Discipline discipline = null;

            String disciplineName = disciplineNameField.getText().trim();
            String lectureHoursText = lectureHoursField.getText().trim();
            String labsCountText = labsCountField.getText().trim();

            if (!disciplineName.isBlank() || !lectureHoursText.isBlank() || !labsCountText.isBlank()) {
                if (disciplineName.isBlank() || lectureHoursText.isBlank() || labsCountText.isBlank()) {
                    throw new IllegalArgumentException("Если дисциплина указана, заполните все поля дисциплины");
                }

                Long lectureHours = Long.parseLong(lectureHoursText);
                int labsCount = Integer.parseInt(labsCountText);

                discipline = new Discipline(disciplineName, lectureHours, labsCount);
            }

            result = new LabWork(
                    0,
                    name,
                    new Coordinates(x, y),
                    new Date(),
                    minimalPoint,
                    personalQualitiesMaximum,
                    difficulty,
                    discipline
            );

            saved = true;
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Проверьте числовые поля",
                    "Ошибка ввода",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Ошибка ввода",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public LabWork getLabWork() {
        return result;
    }
}
