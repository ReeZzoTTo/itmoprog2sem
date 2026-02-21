package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class HelpCommand extends Command {
    public HelpCommand(Context context) {
        super(context);
        this.setName("help");
        this.setDescription(" : вывести справку по доступным командам");
    }

    @Override
    public boolean execute(String[] arguments) {
        this.getContext()
            .getCommandManager()
            .getCommandList()
            .forEach((commandName, command) -> {
            System.out.println(commandName + command.getDescription());
        });
        return true;
    }
}
