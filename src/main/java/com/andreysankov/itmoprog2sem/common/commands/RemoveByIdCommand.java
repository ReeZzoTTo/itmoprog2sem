package com.andreysankov.itmoprog2sem.common.commands;

import java.io.IOException;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class RemoveByIdCommand extends Command implements Savable {
    public RemoveByIdCommand(Context context) {
        super(context);
        this.setName("remove_by_id");
        this.setDescription(" id : удалить элемент из коллекции по его id");
    }

    @Override
    public Response execute(Request request) {
        String argument = request.getArgument();
        if (argument == null) return new Response(false, "Укажите ID элемента\n(Команда remove_by_id требует аргумент)");

        Long id;

        try {
            id = Long.parseLong(argument);
        } catch (NumberFormatException e) {
            return new Response(false, "Аргумент должен быть числом");
        }
        boolean wasDeleted = getContext().getCollectionManager().deleteElementByID(id);

        if (wasDeleted) { 
            String isSave = save();
            if (isSave != null) return new Response(false, "Ошибка сохранения файла: " + isSave);

            return new Response(true, "Элемент с ID = " + id + " успешно удален");
        }
        return new Response(false, "Элемента с данными ID = " + id + " не существует");
    } 

    public String save() {
        try { getContext().getFileManager().saveFile(getContext().getCollectionManager().getCollection()); return null; }
        catch (IOException e) { return e.getMessage(); }
    } 
}
