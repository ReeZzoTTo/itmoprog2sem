package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class RemoveByIdCommand extends AbstractCommand{
    public RemoveByIdCommand(Context context) {
        super(context);
        this.setName("remove_by_id");
        this.setDescription(" id : удалить элемент из коллекции по его id");
    }

    @Override
    public boolean execute() {
        Long id = Long.parseLong(getContext().getCommandManager().getArguments()[1]);

        boolean wasDeleted = getContext().getCollectionManager().deleteElementByID(id);

        if (wasDeleted) { System.out.println("Элемент с ID = " + id + " успешно удален"); }
        else { System.out.println("Элемента с данными ID = " + id + " не существует"); }
        return true;
    } 
}
