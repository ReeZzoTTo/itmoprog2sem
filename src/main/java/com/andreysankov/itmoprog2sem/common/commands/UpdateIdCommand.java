package com.andreysankov.itmoprog2sem.common.commands;

import java.sql.SQLException;

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

        String ownerLogin = request.getLogin();

        if (ownerLogin == null || ownerLogin.isBlank()) {
            return new Response(false, "Не указан логин пользователя");
        }

        LabWork newElement = request.getLabWork();

        if (newElement == null) {
            return new Response(false, "Команда update_id требует объект LabWork");
        }

        LabWork oldElement = getContext()
            .getCollectionManager()
            .getElementByID(id);

        if (oldElement == null) {
            return new Response(false, "Элемента с ID = " + id + " не существует");
        }

        if (!ownerLogin.equals(oldElement.getOwnerLogin())) {
            return new Response(false, "Нельзя обновить элемент с ID = " + id + ": он принадлежит другому пользователю");
        }

        newElement.setId(id);
        newElement.setDate(oldElement.getDate());
        newElement.setOwnerLogin(ownerLogin);

        try {
            boolean updatedInDatabase = getContext()
                .getLabWorkRepository()
                .updateElement(newElement, id, ownerLogin);

            if (!updatedInDatabase) {
                return new Response(false, "Не удалось обновить элемент: объект принадлежит другому пользователю или уже удалён");
            }

            getContext()
                .getCollectionManager()
                .deleteElementByID(id);

            getContext()
                .getCollectionManager()
                .addElement(newElement);

            return new Response(true, "Элемент с ID = " + id + " успешно обновлён");

        } catch (SQLException e) {
            return new Response(false, "Ошибка базы данных при обновлении элемента: " + e.getMessage());
        }
    }
}
