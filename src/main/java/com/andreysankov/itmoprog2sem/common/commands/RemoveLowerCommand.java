package com.andreysankov.itmoprog2sem.common.commands;

import java.io.IOException;
import java.util.Iterator;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.common.models.LabWork;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class RemoveLowerCommand extends Command implements Savable {
    public RemoveLowerCommand(Context context) {
        super(context);
        this.setName("remove_lower");
        this.setDescription(" {element} : удалить из коллекции все элементы, меньшие, чем заданный");
    }

    @Override
    public Response execute(Request request) {
        int removeElementsCount = 0;
        LabWork currentElement = request.getLabWork();

        if (currentElement == null) {
            return new Response(false, "Команда remove_lower требует объект LabWork");
        }

        Iterator<LabWork> iterator = getContext().getCollectionManager().getIterator();

        while (iterator.hasNext()) {
            LabWork element = iterator.next();

            if (element.getMinimalPoint() < currentElement.getMinimalPoint()) {
                iterator.remove();
                System.out.println(element.getId() + " === " + element.getName() + " успешно удалён");
                removeElementsCount++;
            }
        }

        if (removeElementsCount > 0) {
            String isSave = save();
            if (isSave != null) return new Response(false, "Ошибка сохранения файла: " + isSave);
        }

        return new Response(true, "Удалено элементов : " + removeElementsCount);
    } 

    public String save() {
        try { getContext().getFileManager().saveFile(getContext().getCollectionManager().getCollection()); return null; }
        catch (IOException e) { return e.getMessage(); }
    } 
}
