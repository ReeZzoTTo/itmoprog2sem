package com.andreysankov.itmoprog2sem.commands;

import java.io.IOException;

import com.andreysankov.itmoprog2sem.exceptions.AppException;
import com.andreysankov.itmoprog2sem.managers.Context;

public class SaveCommand extends AbstractCommand {
    public SaveCommand(Context context) {
        super(context);
        this.setName("save");
        this.setDescription(" : сохранить коллекцию в файл");
    }

    @Override
    public boolean execute() {
        try {
            getContext().getFileManager().saveFile(getContext().getCollectionManager().getCollection());
            System.out.println("Файл успешно сохранён");
        } catch (IOException e) {
            getContext().getErrorManager().setException(new AppException("Ошибка при сохранении файла: " + e.getMessage()));
            return false;
        }
        
        return true;
    }
}
