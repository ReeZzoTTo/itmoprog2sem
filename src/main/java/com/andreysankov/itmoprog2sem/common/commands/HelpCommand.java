package com.andreysankov.itmoprog2sem.common.commands;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class HelpCommand extends Command {
    public HelpCommand(Context context) {
        super(context);
        this.setName("help");
        this.setDescription(" : вывести справку по доступным командам");
    }

    @Override
    public Response execute(Request request) {
        StringBuilder responseMessage = new StringBuilder();

        this.getContext()
            .getCommandManager()
            .getCommandList()
            .forEach((commandName, command) -> {
            responseMessage.append(commandName + command.getDescription() + "\n");
        });
        return new Response(true, responseMessage.toString());
    }
}
