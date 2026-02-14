package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class ExitCommand extends AbstractCommand {
    public ExitCommand(Context context) {
        super(context);
        this.setName("exit");
        this.setDescription(" : завершить программу (без сохранения в файл)");
    }

    @Override
    public boolean execute() {
        System.exit(0);
        return true;
    }
}
