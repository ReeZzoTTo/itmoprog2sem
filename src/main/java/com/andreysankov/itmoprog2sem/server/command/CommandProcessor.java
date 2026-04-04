package com.andreysankov.itmoprog2sem.server.command;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.andreysankov.itmoprog2sem.common.commands.Command;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;

public class CommandProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);
    private final Map<CommandType, Command> commands = new HashMap<>();

    public void register(CommandType type, Command command) {
        this.commands.put(type, command);
        logger.debug("Зарегистрирована команда: {}", type);
    }

    public Response process(Request request) {
        if (request == null) {
            logger.warn("Получен пустой запрос");
            return new Response(false, "Пустой запрос");
        }

        if (request.getCommandType() == null) {
            logger.warn("В запросе не указан тип команды");
            return new Response(false, "Тип команды не указан");
        }

        Command command = commands.get(request.getCommandType());

        if (command == null) {
            logger.warn("Получена неизвестная команда: {}", request.getCommandType());
            return new Response(false, "Неизвестная команда: " + request.getCommandType());
        }

        logger.info("Выполняется команда: {}", request.getCommandType());
        Response response = command.execute(request);
        logger.info("Команда {} завершена. Успех={}", request.getCommandType(), response.isSuccess());

        return response;
    }
}
