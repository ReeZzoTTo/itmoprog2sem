package com.andreysankov.itmoprog2sem.common.commands;

import java.io.IOException;
import java.util.Date;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class UpdateIdCommand extends Command implements Savable {
    public UpdateIdCommand(Context context) {
        super(context);
        this.setName("update");
        this.setDescription(" id {element} : обновить значение элемента коллекции, id которого равен заданному");
    }

    @Override
    public Response execute(Request request) {
        String argumentId = request.getArgument();
        if (argumentId == null) return new Response(false, "Укажите ID элемента");

        Long id;

        try {
            id = Long.parseLong(argumentId);
        } catch (NumberFormatException e) {
            return new Response(false, "Аргумент должен быть числом");
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
        }

        getContext().getCollectionManager().addElement(newElement);

        String message = wasDeleted ? "Элемент обновлён" : "Создан новый элемент";

        String isSave = save();
        if (isSave != null) return new Response(false, "Ошибка сохранения файла: " + isSave);

        return new Response(true, message);
    }

    public String save() {
        try { getContext().getFileManager().saveFile(getContext().getCollectionManager().getCollection()); return null; }
        catch (IOException e) { return e.getMessage(); }
    } 
}
