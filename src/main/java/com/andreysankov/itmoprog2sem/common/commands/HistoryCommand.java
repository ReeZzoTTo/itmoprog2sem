package com.andreysankov.itmoprog2sem.common.commands;

import java.util.stream.Collectors;

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
        String responseMessage = "";
        responseMessage += "Последние 10 команд\n";

        responseMessage += getContext().getCommandManager().getHistory().stream()
            .collect(Collectors.joining("\n"));

        return new Response(true, responseMessage);
    }
}
