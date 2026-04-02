package com.andreysankov.itmoprog2sem.common.commands;

import java.util.Date;
import java.util.Iterator;

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
        int removeElementsCount = 0;
        LabWork currentElement = request.getLabWork();

        if (currentElement == null) {
            return new Response(false, "Команда remove_lower требует объект LabWork");
        }

        currentElement.setId(getContext().getCollectionManager().generateId());
        currentElement.setDate(new Date());

        Iterator<LabWork> iterator = getContext().getCollectionManager().getIterator();

        while (iterator.hasNext()) {
            LabWork element = iterator.next();

            if (element.getMinimalPoint() < currentElement.getMinimalPoint()) {
                iterator.remove();
                responseMessage += element.getId() + " === " + element.getName() + " успешно удалён\n";
                removeElementsCount++;
            }
        }

        return new Response(true, responseMessage + "Удалено элементов : " + removeElementsCount);
    } 
}
