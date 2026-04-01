package com.andreysankov.itmoprog2sem.common.commands;

import java.util.HashMap;
import java.util.Map;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class GroupCountingByCreationDateCommand extends Command{
    public GroupCountingByCreationDateCommand(Context context) {
        super(context);
        this.setName("group_counting_by_creation_date");
        this.setDescription(" : сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе");
    }

    @Override
    public Response execute(Request request) {
        String responseMessage = "";
        Map<String, Integer> groups = new HashMap<>();
        String dateId;

        for (LabWork element : getContext().getCollectionManager().getCollection()) {
            String[] dateSplit = element.getDate().toString().split(" ");
            dateId = dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2];

            if (!groups.containsKey(dateId)) groups.put(dateId, 1);
            else groups.put(dateId, groups.get(dateId) + 1);
        }

        groups.forEach((dateKey, element) -> {
            responseMessage.concat(dateKey + " --- элементов: " + element + '\n');
        });

        return new Response(true, responseMessage);
    }

}
