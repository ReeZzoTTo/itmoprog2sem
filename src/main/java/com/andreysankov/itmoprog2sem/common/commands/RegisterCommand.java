package com.andreysankov.itmoprog2sem.common.commands;

import java.sql.SQLException;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class RegisterCommand extends Command {
    public RegisterCommand(Context context) {
        super(context);
        this.setName("register");
        this.setDescription(" : зарегистрировать нового пользователя");
    }

    @Override
    public Response execute(Request request) {
        String login = request.getLogin();
        String password = request.getPassword();

        if (login == null || login.isBlank()) {
            return new Response(false, "Логин не может быть пустым");
        }

        if (password == null || password.isBlank()) {
            return new Response(false, "Пароль не может быть пустым");
        }

        try {
            boolean isLoginExists = getContext().getUserRepository().existsByLogin(login);

            if (isLoginExists) {
                return new Response(false, "Логин занят");
            }
            boolean registered = getContext().getUserRepository().register(login, password);

            if (registered) {
                return new Response(true, "Пользователь успешно зарегистрирован");
            }

            return new Response(false, "Не удалось зарегистрировать пользователя");

        } catch (SQLException e) {
            return new Response(false, "Ошибка при регистрации пользователя: " + e.getMessage());
        }
    }
}
