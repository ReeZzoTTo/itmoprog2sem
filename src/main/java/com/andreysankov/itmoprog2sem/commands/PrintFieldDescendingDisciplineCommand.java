package com.andreysankov.itmoprog2sem.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.models.Discipline;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class PrintFieldDescendingDisciplineCommand extends AbstractCommand{
    public PrintFieldDescendingDisciplineCommand(Context context) {
        super(context);
        this.setName("print_field_descending_discipline");
        this.setDescription(" : вывести значения поля discipline всех элементов в порядке убывания");
    }

    @Override
    public boolean execute() {
        Set<Discipline> disciplineSet = new HashSet<>();
        Iterator<LabWork> iterator = getContext().getCollectionManager().getIterator();
        
        while (iterator.hasNext()) {
            LabWork element = iterator.next();

            if (element.getDiscipline() != null) {
                disciplineSet.add(element.getDiscipline());
            }
        }

        List<Discipline> disciplineList = new ArrayList<>(disciplineSet);

        disciplineList.sort(
            Comparator.comparing(
                Discipline::getName,
                Comparator.nullsLast(Comparator.reverseOrder())
            )
        );

        for (Discipline discipline : disciplineList) {
            System.out.println("Discipline : " + discipline.getName());
            System.out.println("             Lecture hours : " + discipline.getLectureHours());
            System.out.println("             Labs count    : " + discipline.getLabsCount() + "\n");
        }

        return true;
    }  
}
