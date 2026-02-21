package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class ExitCommand extends Command {
    public ExitCommand(Context context) {
        super(context);
        this.setName("exit");
        this.setDescription(" : завершить программу (без сохранения в файл)");
    }

    @Override
    public boolean execute(String[] arguments) {
        System.out.println("Завершение сеанса.\nGoodBye :)");
        System.exit(0);
        return true;
    }
}
