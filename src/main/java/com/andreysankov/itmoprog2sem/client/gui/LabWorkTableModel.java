package com.andreysankov.itmoprog2sem.client.gui;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.table.AbstractTableModel;

import com.andreysankov.itmoprog2sem.common.models.Discipline;
import com.andreysankov.itmoprog2sem.common.models.LabWork;

public class LabWorkTableModel extends AbstractTableModel {
    private final List<LabWork> allLabWorks = new ArrayList<>();
    private final List<LabWork> visibleLabWorks = new ArrayList<>();
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

    private int filterColumn = -1;
    private String filterText = "";

    private int sortColumn = -1;
    private boolean sortAscending = true;

    public void setLocale(Locale locale) {
        this.locale = locale;
        fireTableDataChanged();
    }

    public void setLabWorks(List<LabWork> newLabWorks) {
        allLabWorks.clear();

        if (newLabWorks != null) {
            allLabWorks.addAll(newLabWorks);
        }

        applyFilterAndSort();
    }

    public void setFilter(int column, String text) {
        this.filterColumn = column;
        this.filterText = text == null ? "" : text.trim().toLowerCase();

        applyFilterAndSort();
    }

    public void setSort(int column, boolean ascending) {
        this.sortColumn = column;
        this.sortAscending = ascending;

        applyFilterAndSort();
    }

    public LabWork getLabWorkAt(int rowIndex) {
        return visibleLabWorks.get(rowIndex);
    }

    public List<LabWork> getVisibleLabWorks() {
        return Collections.unmodifiableList(visibleLabWorks);
    }

    public String[] getColumns() {
        return columns;
    }

    private void applyFilterAndSort() {
        visibleLabWorks.clear();

        List<LabWork> result = allLabWorks.stream()
                .filter(this::matchesFilter)
                .collect(Collectors.toList());

        if (sortColumn >= 0) {
            Comparator<LabWork> comparator = getComparator(sortColumn);

            if (!sortAscending) {
                comparator = comparator.reversed();
            }

            result = result.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        visibleLabWorks.addAll(result);
        fireTableDataChanged();
    }

    private boolean matchesFilter(LabWork labWork) {
        if (filterColumn < 0 || filterText.isBlank()) {
            return true;
        }

        String value = String.valueOf(getValueForFilter(labWork, filterColumn)).toLowerCase();

        return value.contains(filterText);
    }

    private Object getValueForFilter(LabWork labWork, int columnIndex) {
        Discipline discipline = labWork.getDiscipline();

        return switch (columnIndex) {
            case 0 -> labWork.getId();
            case 1 -> labWork.getName();
            case 2 -> labWork.getCoordinates() == null ? "" : labWork.getCoordinates().getX();
            case 3 -> labWork.getCoordinates() == null ? "" : labWork.getCoordinates().getY();
            case 4 -> labWork.getDate();
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

    private Comparator<LabWork> getComparator(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Comparator.comparing(LabWork::getId);
            case 1 -> Comparator.comparing(LabWork::getName, Comparator.nullsLast(String::compareToIgnoreCase));
            case 2 -> Comparator.comparing(labWork ->
                    labWork.getCoordinates() == null ? null : labWork.getCoordinates().getX(),
                    Comparator.nullsLast(Long::compareTo)
            );
            case 3 -> Comparator.comparing(labWork ->
                    labWork.getCoordinates() == null ? null : labWork.getCoordinates().getY(),
                    Comparator.nullsLast(Integer::compareTo)
            );
            case 4 -> Comparator.comparing(LabWork::getDate, Comparator.nullsLast(java.util.Date::compareTo));
            case 5 -> Comparator.comparing(LabWork::getMinimalPoint);
            case 6 -> Comparator.comparing(LabWork::getPersonalQualitiesMaximum, Comparator.nullsLast(Double::compareTo));
            case 7 -> Comparator.comparing(labWork ->
                    labWork.getDifficulty() == null ? "" : labWork.getDifficulty().name()
            );
            case 8 -> Comparator.comparing(labWork -> {
                Discipline discipline = labWork.getDiscipline();
                return discipline == null ? "" : discipline.getName();
            }, Comparator.nullsLast(String::compareToIgnoreCase));
            case 9 -> Comparator.comparing(labWork -> {
                Discipline discipline = labWork.getDiscipline();
                return discipline == null ? null : discipline.getLectureHours();
            }, Comparator.nullsLast(Long::compareTo));
            case 10 -> Comparator.comparing(labWork -> {
                Discipline discipline = labWork.getDiscipline();
                return discipline == null ? null : discipline.getLabsCount();
            }, Comparator.nullsLast(Integer::compareTo));
            case 11 -> Comparator.comparing(LabWork::getOwnerLogin, Comparator.nullsLast(String::compareToIgnoreCase));
            default -> Comparator.comparing(LabWork::getId);
        };
    }

    @Override
    public int getRowCount() {
        return visibleLabWorks.size();
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
        LabWork labWork = visibleLabWorks.get(rowIndex);
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
