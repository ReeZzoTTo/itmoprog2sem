package com.andreysankov.itmoprog2sem.common.commands;

import java.sql.SQLException;

import com.andreysankov.itmoprog2sem.common.dto.Request;
import com.andreysankov.itmoprog2sem.common.dto.Response;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class LoginCommand extends Command {
        public LoginCommand(Context context) {
        super(context);
        this.setName("login");
        this.setDescription(" : логин");
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
            boolean authorized = getContext().getUserRepository().checkCredentials(login, password);

            if (authorized) {
                return new Response(true, "Вход выполнен успешно");
            }

            return new Response(false, "Неверный логин или пароль");

        } catch (SQLException e) {
            return new Response(false, "Ошибка при авторизации пользователя: " + e.getMessage());
        }
    }
}
