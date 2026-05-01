package com.andreysankov.itmoprog2sem.common.commands;

import java.util.Map;
import java.util.stream.Collectors;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class GroupCountingByCreationDateCommand extends Command{
    public GroupCountingByCreationDateCommand(Context context) {
        super(context);
        this.setName("group_counting_by_creation_date");
        this.setDescription(" : сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе");
    }

    @Override
    public Response execute(Request request) {

        if (getContext().getCollectionManager().getCollectionSize() == 0) {
            return new Response(true, "Коллекция пуста");
        }

        Map<String, Long> groups = getContext().getCollectionManager().getCollection().stream()
            .collect(Collectors.groupingBy(
                element -> {
                    String[] dateSplit = element.getDate().toString().split(" ");
                    return dateSplit[0] + " " + dateSplit[1] + " " + dateSplit[2];
                }, Collectors.counting()
            ));

        String responseMessage = groups.entrySet().stream()
            .map(entry -> entry.getKey() + " --- элементов: " + entry.getValue())
            .collect(Collectors.joining("\n"));

        return new Response(true, responseMessage);
    }
}
