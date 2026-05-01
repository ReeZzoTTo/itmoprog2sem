package com.andreysankov.itmoprog2sem.common.commands;

import java.sql.SQLException;
import java.util.Date;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class AddCommand extends Command {
    public AddCommand(Context context) {
        super(context);
        this.setName("add");
        this.setDescription(" {element} : добавить новый элемент в коллекцию");
    }

    @Override
    public Response execute(Request request) {
        LabWork labWork = request.getLabWork();

        if (labWork == null) {
            return new Response(false, "Команда add требует объект LabWork");
        }

        String ownerLogin = request.getLogin();

        if (ownerLogin == null || ownerLogin.isBlank()) {
            return new Response(false, "Не указан логин пользователя");
        }
 
        labWork.setDate(new Date());
        labWork.setOwnerLogin(ownerLogin);

        try {
            long generatedId = getContext().getLabWorkRepository().insertLabWork(labWork, ownerLogin);
            labWork.setId(generatedId);
            
            getContext().getCollectionManager().addElement(labWork);
            
            return new Response(true, "Элемент успешно добавлен");

        } catch (SQLException e) {
            return new Response(false, "Не удалось добавить элемент в БД: " + e.getMessage());
        }
    }   
}
