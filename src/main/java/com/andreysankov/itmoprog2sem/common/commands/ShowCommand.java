package com.andreysankov.itmoprog2sem.common.commands;

import java.util.stream.Collectors;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class ShowCommand extends Command {
    public ShowCommand(Context context) {
        super(context);
        this.setName("show");
        this.setDescription(" : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public Response execute(Request request) {
        if (getContext().getCollectionManager().getCollectionSize() == 0) {
            return new Response(true, "Коллекция пуста");
        }

        String responseMessage = getContext().getCollectionManager().getCollection().stream()
            .sorted()    
            .map(Object::toString)
            .collect(Collectors.joining("\n"));

        return new Response(true, responseMessage);
    }
}
