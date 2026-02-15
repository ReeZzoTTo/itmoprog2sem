package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class SaveCommand extends AbstractCommand {
    public SaveCommand(Context context) {
        super(context);
        this.setName("save");
        this.setDescription(" : сохранить коллекцию в файл");
    }

    @Override
    public boolean execute() {
        
        return true;
    }

    
}
