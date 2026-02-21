package com.andreysankov.itmoprog2sem.commands;

import java.util.List;

import com.andreysankov.itmoprog2sem.managers.Context;

public class HistoryCommand extends Command {
    public HistoryCommand(Context context) {
        super(context);
        this.setName("history");
        this.setDescription(" : вывести последние 10 команд (без их аргументов)");
    }

    @Override
    public boolean execute(String[] arguments) {
        System.out.println("Последние 10 команд");

        List<String> history = getContext().getCommandManager().getHistory();
        
        for (String command : history) {
            System.out.println(command);
        }

        return true;
    }
}
