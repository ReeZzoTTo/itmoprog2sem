package com.andreysankov.itmoprog2sem.common.commands;

import java.sql.SQLException;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class ClearCommand extends Command {
    public ClearCommand(Context context) {
        super(context);
        this.setName("clear");
        this.setDescription(" : удалить из коллекции все принадлежащие вам элементы");
    }

    @Override
    public Response execute(Request request) {
        String ownerLogin = request.getLogin();

        if (ownerLogin == null || ownerLogin.isBlank()) {
            return new Response(false, "Не указан логин пользователя");
        }

        try {
            getContext().getLabWorkRepository().deleteAllByOwner(ownerLogin);

            int deletedFromMemory = getContext().getCollectionManager().deleteElementsByOwner(ownerLogin);

            return new Response(
                true,
                "Удалено ваших элементов: " + deletedFromMemory
            );

        } catch (SQLException e) {
            return new Response(false, "Ошибка базы данных при очистке коллекции: " + e.getMessage());
        }
    }
}
