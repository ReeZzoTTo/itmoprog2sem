package com.andreysankov.itmoprog2sem.common.commands;

import java.io.IOException;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class AddIfMinCommand extends Command implements Savable {
    public AddIfMinCommand(Context context) {
        super(context);
        this.setName("add_if_min");
        this.setDescription(" {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
    }

    @Override
    public Response execute(Request request) {
        LabWork labWork = request.getLabWork();

        if (labWork == null) {
            return new Response(false, "Команда add_if_min требует объект LabWork");
        }

        int minimum = this.getMinOfMinimalPoint();

        if (minimum == -1 || labWork.getMinimalPoint() < minimum) {
            getContext().getCollectionManager().addElement(labWork);

            String isSave = save();
            if (isSave != null) return new Response(false, "Ошибка сохранения файла: " + isSave);
        
            return new Response(true, "Элемент успешно добавлен в коллекцию");
        }
        return new Response(false, "Элемент с указанным значением minimalPoint=" + labWork.getMinimalPoint() + " не является наименьшим\nЭлемент не был добавлен");
    }

    public int getMinOfMinimalPoint() {
        int minimum = -1;

        for (LabWork element : getContext().getCollectionManager().getCollection()) {
            int currentMinimalPoint = element.getMinimalPoint();
            
            if (minimum == -1) minimum = currentMinimalPoint;
            else {
                if (currentMinimalPoint < minimum) minimum = currentMinimalPoint;
            }
        }

        return minimum;
    }

    public String save() {
        try { getContext().getFileManager().saveFile(getContext().getCollectionManager().getCollection()); return null; }
        catch (IOException e) { return e.getMessage(); }
    } 
}
