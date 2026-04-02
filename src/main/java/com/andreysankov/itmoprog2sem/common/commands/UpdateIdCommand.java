package com.andreysankov.itmoprog2sem.common.commands;

import java.util.Date;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class UpdateIdCommand extends Command {
    public UpdateIdCommand(Context context) {
        super(context);
        this.setName("update_id");
        this.setDescription(" id {element} : обновить значение элемента коллекции, id которого равен заданному");
    }

    @Override
    public Response execute(Request request) {
        Long id = request.getArgument().getId();
        
        if (id == 0) {
            return new Response(false, request.getArgument().getResponseMessage());
        }

        LabWork newElement = request.getLabWork();
        if (newElement == null) return new Response(false, "");

        Date currentCreationDate;
        LabWork targetELement = getContext().getCollectionManager().getElementByID(id);

        boolean wasDeleted = getContext().getCollectionManager().deleteElementByID(id);

        newElement.setId(id);

        if (targetELement != null) {
            currentCreationDate = targetELement.getDate();
            newElement.setDate(currentCreationDate);
        } else {
            newElement.setDate(new Date());
        }

        getContext().getCollectionManager().addElement(newElement);

        String message = wasDeleted ? "Элемент обновлён" : "Создан новый элемент";

        return new Response(true, message);
    }
}
