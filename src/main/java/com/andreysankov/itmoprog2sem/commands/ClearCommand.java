package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class ClearCommand extends Command{
    public ClearCommand(Context context) {
        super(context);
        this.setName("clear");
        this.setDescription(" : очистить коллекцию");
    }

    @Override
    public boolean execute(String[] arguments) {
        getContext().getCollectionManager().clearCollection();
        System.out.println("Коллекция очищена");
        return true;
    }
}
