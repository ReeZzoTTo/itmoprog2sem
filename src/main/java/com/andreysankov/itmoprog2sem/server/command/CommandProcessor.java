package com.andreysankov.itmoprog2sem.server.command;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.andreysankov.itmoprog2sem.common.commands.Command;
import com.andreysankov.itmoprog2sem.common.dto.CommandType;
import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class CommandProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);
    private final Map<CommandType, Command> commands = new HashMap<>();
    private final Context context;

    public CommandProcessor(Context context) {
        this.context = context;
    }

    public void register(CommandType type, Command command) {
        this.commands.put(type, command);
        logger.debug("Зарегистрирована команда: {}", type);
    }

    private boolean requiresAuthorization(CommandType commandType) {
        return commandType != CommandType.REGISTER && commandType != CommandType.LOGIN;
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

        if (this.requiresAuthorization(request.getCommandType())) {
            Response authResponse = authorize(request);
        
            if (!authResponse.isSuccess()) {
                logger.warn(
                    "Отказано в выполнении команды {}: {}",
                    request.getCommandType(),
                    authResponse.getMessage()
                );
                return authResponse;
            }
        }

        logger.info("Выполняется команда: {}", request.getCommandType());
        Response response = command.execute(request);
        logger.info("Команда {} завершена. Успех={}", request.getCommandType(), response.isSuccess());

        return response;
    }

    private Response authorize(Request request) {
        String login = request.getLogin();
        String password = request.getPassword();

        if (login == null || login.isBlank()) {
            return new Response(false, "Для выполнения команды необходимо указать логин");
        }

        if (password == null || password.isBlank()) {
            return new Response(false, "Для выполнения команды необходимо указать пароль");
        }
        
        try {
            boolean authorized = this.context.getUserRepository().checkCredentials(login, password);

            if (!authorized) {
                return new Response(false, "Команда недоступна: пользователь не авторизован");
            }

            return new Response(true, "Пользователь авторизован");

        } catch (SQLException e) {
            return new Response(false, "Ошибка проверки авторизации: " + e.getMessage());
        }
    }
}
