package com.andreysankov.itmoprog2sem.common.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.Discipline;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class PrintFieldDescendingDisciplineCommand extends Command{
    public PrintFieldDescendingDisciplineCommand(Context context) {
        super(context);
        this.setName("print_field_descending_discipline");
        this.setDescription(" : вывести значения поля discipline всех элементов в порядке убывания");
    }

    @Override
    public Response execute(Request request) {
        String responseMessage = "";
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
            responseMessage.concat("Discipline : " + discipline.getName() + "\n" 
                + "             Lecture hours : " + discipline.getLectureHours() + "\n"
                + "             Labs count    : " + discipline.getLabsCount() + "\n"
            );
        }

        return new Response(true, responseMessage);
    }  
}
