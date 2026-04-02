package com.andreysankov.itmoprog2sem.common.commands;

import java.util.List;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class HistoryCommand extends Command {
    public HistoryCommand(Context context) {
        super(context);
        this.setName("history");
        this.setDescription(" : вывести последние 10 команд (без их аргументов)");
    }

    @Override
    public Response execute(Request request) {
        StringBuilder responseMessage = new StringBuilder();
        responseMessage.append("Последние 10 команд");

        List<String> history = getContext().getCommandManager().getHistory();
        
        for (String command : history) {
           responseMessage.append(command + "\n");
        }

        return new Response(true, responseMessage.toString());
    }
}
