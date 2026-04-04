package com.andreysankov.itmoprog2sem.common.commands;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class RemoveLowerCommand extends Command {
    public RemoveLowerCommand(Context context) {
        super(context);
        this.setName("remove_lower");
        this.setDescription(" {element} : удалить из коллекции все элементы, меньшие, чем заданный");
    }

    @Override
    public Response execute(Request request) {
        String responseMessage = "";
        LabWork currentElement = request.getLabWork();

        if (currentElement == null) {
            return new Response(false, "Команда remove_lower требует объект LabWork");
        }

        currentElement.setId(getContext().getCollectionManager().generateId());
        currentElement.setDate(new Date());

        LinkedHashSet<LabWork> collection = getContext().getCollectionManager().getCollection();

        responseMessage += collection.stream()
            .filter(element -> element.getMinimalPoint() < currentElement.getMinimalPoint())
            .map(element -> "ID-" + element.getId() + " === " + element.getName() + " успешно удалён")
            .collect(Collectors.joining("\n"));

        long removeElementsCount = collection.stream()
            .filter(element -> element.getMinimalPoint() < currentElement.getMinimalPoint())
            .count();

        LinkedHashSet<LabWork> filteredCollection = collection.stream()
            .filter(element -> element.getMinimalPoint() >= currentElement.getMinimalPoint())
            .collect(Collectors.toCollection(LinkedHashSet::new));

        getContext().getCollectionManager().setCollection(filteredCollection);

        return new Response(true, responseMessage + "\nУдалено элементов : " + removeElementsCount);
    } 
}
