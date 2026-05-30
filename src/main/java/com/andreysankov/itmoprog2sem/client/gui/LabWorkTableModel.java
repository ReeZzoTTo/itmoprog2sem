package com.andreysankov.itmoprog2sem.client.gui;

import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.andreysankov.itmoprog2sem.common.models.Discipline;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class LabWorkTableModel extends AbstractTableModel {
    private final List<LabWork> labWorks = new ArrayList<>();
    private Locale locale = new Locale("ru", "RU");
    private final String[] columns = {
        "ID",
        "Название",
        "X",
        "Y",
        "Дата создания",
        "Min points",
        "Max qualities",
        "Difficulty",
        "Discipline name",
        "Lecture hours",
        "Labs count",
        "Owner"
    };

    public void setLocale(Locale locale) {
        this.locale = locale;
        fireTableDataChanged();
    }

    public void setLabWorks(List<LabWork> newLabWorks) {
        labWorks.clear();

        if (newLabWorks != null) {
            labWorks.addAll(newLabWorks);
        }

        fireTableDataChanged();
    }

    public LabWork getLabWorkAt(int rowIndex) {
        return labWorks.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return labWorks.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LabWork labWork = labWorks.get(rowIndex);
        Discipline discipline = labWork.getDiscipline();

        DateFormat dateFormat = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM,
                locale
        );

        return switch (columnIndex) {
            case 0 -> labWork.getId();
            case 1 -> labWork.getName();
            case 2 -> labWork.getCoordinates() == null ? "" : labWork.getCoordinates().getX();
            case 3 -> labWork.getCoordinates() == null ? "" : labWork.getCoordinates().getY();
            case 4 -> labWork.getDate() == null ? "" : dateFormat.format(labWork.getDate());
            case 5 -> labWork.getMinimalPoint();
            case 6 -> labWork.getPersonalQualitiesMaximum();
            case 7 -> labWork.getDifficulty();
            case 8 -> discipline == null ? "" : discipline.getName();
            case 9 -> discipline == null ? "" : discipline.getLectureHours();
            case 10 -> discipline == null ? "" : discipline.getLabsCount();
            case 11 -> labWork.getOwnerLogin();
            default -> "";
        };
    }
}
