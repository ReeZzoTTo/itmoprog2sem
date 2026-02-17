package com.andreysankov.itmoprog2sem.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
        
        List<Discipline> disciplineList = new ArrayList<>();
        Iterator<LabWork> iterator = getContext().getCollectionManager().getIterator();
        
        while (iterator.hasNext()) {
            LabWork element = iterator.next();

            if (element.getDiscipline() != null) {
                disciplineList.add(element.getDiscipline());
            }
        }

        Collections.sort(disciplineList, new Comparator<Discipline>() {
            @Override
            public int compare(Discipline d1, Discipline d2) {
                return d2.getName().compareTo(d1.getName());
            }
        });

        for (Discipline discipline : disciplineList) {
            System.out.println("Discipline : " + discipline.getName());
            System.out.println("             Lecture hours : " + discipline.getLectureHours());
            System.out.println("             Labs count    : " + discipline.getLabsCount() + "\n");
        }

        return true;
    }  
}
