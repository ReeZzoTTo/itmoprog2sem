package com.andreysankov.itmoprog2sem.server.command;

import java.util.HashMap;
import java.util.Map;

import com.andreysankov.itmoprog2sem.common.commands.Command;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;

public class CommandProcessor {
    private final Map<CommandType, Command> commands = new HashMap<>();

    public void register(CommandType type, Command command) {
        this.commands.put(type, command);
    }

    public Response process(Request request) {
        if (request == null) {
            return new Response(false, "Пустой запрос");
        }

        if (request.getCommandType() == null) {
            return new Response(false, "Тип команды не указан");
        }

        Command command = commands.get(request.getCommandType());

        if (command == null) {
            return new Response(false, "Неизвестная команда: " + request.getCommandType());
        }

        return command.execute(request);
    }
}
