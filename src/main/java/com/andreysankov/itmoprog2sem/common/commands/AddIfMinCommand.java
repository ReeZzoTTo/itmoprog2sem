package com.andreysankov.itmoprog2sem.common.commands;

import java.util.Comparator;
import java.util.Date;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class AddIfMinCommand extends Command {
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

        labWork.setId(getContext().getCollectionManager().generateId());
        labWork.setDate(new Date());

        LabWork minLabWork = getContext().getCollectionManager().getCollection().stream()
            .min(Comparator.comparing(LabWork::getMinimalPoint))
            .orElse(null);

        if (minLabWork == null || labWork.getMinimalPoint() < minLabWork.getMinimalPoint()) {
            getContext().getCollectionManager().addElement(labWork);

            return new Response(true, "Элемент успешно добавлен в коллекцию");
        }
        return new Response(false, "Элемент с указанным значением minimalPoint=" + labWork.getMinimalPoint() + " не является наименьшим\nЭлемент не был добавлен");
    }
}
