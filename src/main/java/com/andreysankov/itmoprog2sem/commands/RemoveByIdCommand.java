package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.exceptions.AppException;
import com.andreysankov.itmoprog2sem.managers.Context;

public class RemoveByIdCommand extends AbstractCommand{
    public RemoveByIdCommand(Context context) {
        super(context);
        this.setName("remove_by_id");
        this.setDescription(" id : удалить элемент из коллекции по его id");
    }

    @Override
    public boolean execute() {
        String argument = getContext().getCommandManager().getArgument(1, "Укажите ID элемента\n(Команда remove_by_id требует аргумент)");
        if (argument == null) return false;

        Long id;

        try {
            id = Long.parseLong(argument);
        } catch (NumberFormatException e) {
            getContext().getErrorManager().setException(e);
            return false;
        }
        boolean wasDeleted = getContext().getCollectionManager().deleteElementByID(id);

        if (wasDeleted) { 
            System.out.println("Элемент с ID = " + id + " успешно удален"); 
            return true;
        }
        
        getContext().getErrorManager().setException(new AppException("Элемента с данными ID = " + id + " не существует"));
        return false;
    } 
}
