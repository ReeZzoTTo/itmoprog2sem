package com.andreysankov.itmoprog2sem.server.console;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.andreysankov.itmoprog2sem.common.commands.SaveCommand;
import com.andreysankov.itmoprog2sem.server.managers.Context;

public class ServerConsoleHandler {
    private final Map<String, Supplier<String>> commands = new HashMap<>();

    public ServerConsoleHandler(Context context) {
        commands.put("save", () -> {
            String error = SaveCommand.save(context);
            if (error == null) {
                return "Коллекция успешно сохранена.";
            }
            return "Ошибка сохранения: " + error;
        });
        commands.put("exit", () -> {
            String message = this.commands.get("save").get();
            System.out.println(message);
            System.out.println("Завершение сервера");
            System.exit(0);
            
            return ""; 
        });
    }

    public void handle(String line) {
        if (line == null) return;

        String command = line.trim();

        if (command.isEmpty()) return;

        Supplier<String> action = commands.get(command);

        if (action == null) {
            System.out.println("Неизвестная серверная команда.");
            return;
        }

        System.out.println(action.get());
    }
}