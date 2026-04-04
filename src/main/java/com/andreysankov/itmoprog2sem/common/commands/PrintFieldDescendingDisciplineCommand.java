package com.andreysankov.itmoprog2sem.common.commands;

import java.util.Comparator;
import java.util.stream.Collectors;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.Discipline;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class PrintFieldDescendingDisciplineCommand extends Command{
    public PrintFieldDescendingDisciplineCommand(Context context) {
        super(context);
        this.setName("print_field_descending_discipline");
        this.setDescription(" : вывести значения поля discipline всех элементов в порядке убывания");
    }

    @Override
    public Response execute(Request request) {
        String responseMessage = getContext().getCollectionManager().getCollection().stream()
            .map(labwork -> labwork.getDiscipline())    
            .filter(element -> element != null)
            .distinct()
            .sorted(
                Comparator.comparing(
                    Discipline::getName,
                    Comparator.nullsLast(Comparator.reverseOrder())
                )
            )
            .map(discipline -> "Discipline : " + discipline.getName() + "\n" 
                + "             Lecture hours : " + discipline.getLectureHours() + "\n"
                + "             Labs count    : " + discipline.getLabsCount() + "\n"
            )
            .collect(Collectors.joining("\n"));

        return new Response(true, responseMessage);
    }  
}
