package com.andreysankov.itmoprog2sem.common.commands;

import java.sql.SQLException;
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
        LabWork currentElement = request.getLabWork();

        if (currentElement == null) {
            return new Response(false, "Команда remove_lower требует объект LabWork");
        }

        String ownerLogin = request.getLogin();

        if (ownerLogin == null || ownerLogin.isBlank()) {
            return new Response(false, "Не указан логин пользователя");
        }

        int minimalPoint = currentElement.getMinimalPoint();

        LinkedHashSet<LabWork> collection = getContext()
            .getCollectionManager()
            .getCollection();

        String removedElementsMessage = collection.stream()
            .filter(element -> ownerLogin.equals(element.getOwnerLogin()))
            .filter(element -> element.getMinimalPoint() < minimalPoint)
            .map(element -> "ID-" + element.getId() + " === " + element.getName() + " успешно удалён")
            .collect(Collectors.joining("\n"));

        try {
            int deletedFromDatabase = getContext()
                .getLabWorkRepository()
                .deleteLowerByOwner(minimalPoint, ownerLogin);

            getContext()
                .getCollectionManager()
                .deleteLowerByOwner(minimalPoint, ownerLogin);

            if (deletedFromDatabase == 0) {
                return new Response(false, "Нет ваших элементов, меньших чем заданный");
            }

            return new Response(
                true,
                removedElementsMessage + "\nУдалено ваших элементов: " + deletedFromDatabase
            );

        } catch (SQLException e) {
            return new Response(false, "Ошибка базы данных при удалении элементов: " + e.getMessage());
        }
    }
}
