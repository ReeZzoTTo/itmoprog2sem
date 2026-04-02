package com.andreysankov.itmoprog2sem.common.commands;

import java.io.IOException;

import com.andreysankov.itmoprog2sem.server.managers.Context;

public class SaveCommand {
    private SaveCommand() {}

    public static String save(Context context) {
        try { 
            context.getCollectionManager().sortCollection();
            context.getFileManager().saveFile(context.getCollectionManager().getCollection()); 
            return null; 
        }
        catch (IOException e) { 
            return e.getMessage(); 
        }
    }
}
