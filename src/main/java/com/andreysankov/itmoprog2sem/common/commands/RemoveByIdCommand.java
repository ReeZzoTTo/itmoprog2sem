package com.andreysankov.itmoprog2sem.common.commands;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class RemoveByIdCommand extends Command {
    public RemoveByIdCommand(Context context) {
        super(context);
        this.setName("remove_by_id");
        this.setDescription(" id : удалить элемент из коллекции по его id");
    }

    @Override
    public Response execute(Request request) {
        Long id = request.getArgument().getId();
        
        if (id == 0) {
            return new Response(false, request.getArgument().getResponseMessage());
        }

        boolean wasDeleted = getContext().getCollectionManager().deleteElementByID(id);

        if (wasDeleted) { 
            return new Response(true, "Элемент с ID = " + id + " успешно удален");
        }
        return new Response(false, "Элемента с данными ID = " + id + " не существует");
    } 
}
