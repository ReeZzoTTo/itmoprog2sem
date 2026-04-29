package com.andreysankov.itmoprog2sem.common.commands;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class InfoCommand extends Command {
    public InfoCommand(Context context) {
        super(context);
        this.setName("info");
        this.setDescription(" : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }

    @Override
    public Response execute(Request request) {
        String responseMessage = "Сведения о коллекции" + "\n"
        // + "Имя файла коллекции : " + getContext().getFileManager().getFileName() + "\n"
        + "Дата инициализации коллекции : " + getContext().getCollectionManager().getInitializationDate() + "\n"
        + "Количество элементов коллекции : " + getContext().getCollectionManager().getCollectionSize() + "\n";

        return new Response(true, responseMessage);
    }
}
