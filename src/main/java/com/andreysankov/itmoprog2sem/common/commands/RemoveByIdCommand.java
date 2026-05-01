package com.andreysankov.itmoprog2sem.common.commands;

import java.sql.SQLException;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
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

        String ownerLogin = request.getLogin();

        if (ownerLogin == null || ownerLogin.isBlank()) {
            return new Response(false, "Не указан логин пользователя");
        }

        LabWork targetElement = getContext().getCollectionManager().getElementByID(id);

        if (targetElement == null) {
            return new Response(false, "Элемента с данными ID = " + id + " не существует");
        }

        try {
            boolean deletedFromDatabase = getContext().getLabWorkRepository().deleteElement(id, ownerLogin);

            if (!deletedFromDatabase) {
                return new Response(false, "Нельзя удалить элемент с ID = " + id + ": он принадлежит другому пользователю либо уже был удалён");
            }

            getContext().getCollectionManager().deleteElementByID(id);
            
            return new Response(true, "Элемент с ID = " + id + " успешно удален");

        } catch (SQLException e) {
            return new Response(false, "Ошибка базы данных при удалении элемента: " + e.getMessage());   
        }
    } 
}
