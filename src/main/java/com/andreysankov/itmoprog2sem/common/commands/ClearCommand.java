package com.andreysankov.itmoprog2sem.common.commands;

import java.io.IOException;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class ClearCommand extends Command implements Savable {
    public ClearCommand(Context context) {
        super(context);
        this.setName("clear");
        this.setDescription(" : очистить коллекцию");
    }

    @Override
    public Response execute(Request request) {
        getContext().getCollectionManager().clearCollection();

        String isSave = save();
        if (isSave != null) return new Response(false, "Ошибка сохранения файла: " + isSave);

        return new Response(true, "Коллекция очищена");
    }

    public String save() {
        try { getContext().getFileManager().saveFile(getContext().getCollectionManager().getCollection()); return null; }
        catch (IOException e) { return e.getMessage(); }
    } 
}
