package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class ClearCommand extends AbstractCommand{
    public ClearCommand(Context context) {
        super(context);
        this.setName("clear");
        this.setDescription(" : очистить коллекцию");
    }

    @Override
    public boolean execute() {
        getContext().getCollectionManager().clearCollection();
        return true;
    }
}
