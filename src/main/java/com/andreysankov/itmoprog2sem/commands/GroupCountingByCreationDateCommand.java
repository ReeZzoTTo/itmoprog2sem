package com.andreysankov.itmoprog2sem.commands;

import java.util.HashMap;
import java.util.Map;

import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class GroupCountingByCreationDateCommand extends AbstractCommand{
    public GroupCountingByCreationDateCommand(Context context) {
        super(context);
        this.setName("group_counting_by_creation_date");
        this.setDescription(" : сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе");
    }

    @Override
    public boolean execute() {
        Map<String, Integer> groups = new HashMap<>();
        String dateId;

        for (LabWork element : getContext().getCollectionManager().getCollection()) {
            String[] dateSplit = element.getDate().toString().split(" ");
            dateId = dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2];

            if (!groups.containsKey(dateId)) groups.put(dateId, 1);
            else groups.put(dateId, groups.get(dateId) + 1);
        }

        groups.forEach((dateKey, element) -> {
            System.out.println(dateKey + " --- элементов: " + element);
        });

        return true;
    }

}
