package com.andreysankov.itmoprog2sem.commands;

import java.util.Date;

import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.managers.InputManager;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class UpdateIdCommand extends AbstractCommand {
    public UpdateIdCommand(Context context) {
        super(context);
        this.setName("update");
        this.setDescription(" id {element} : обновить значение элемента коллекции, id которого равен заданному");
    }

    @Override
    public boolean execute() {
        String argumentId = getContext().getCommandManager().getArgument(1, "Укажите ID элемента\n(Команда update требует аргумент)");
        if (argumentId == null) return false;
        String argumentUniqueName = getContext().getCommandManager().getArgument(2, "Укажите уникальное имя элемента\n(Команда update требует аргумент)");
        if (argumentUniqueName == null) return false;

        Long id = Long.parseLong(argumentId);
        Date currentCreationDate;
        LabWork targetELement = getContext().getCollectionManager().getElementByID(id);

        boolean wasDeleted = getContext().getCollectionManager().deleteElementByID(id);

        InputManager inputManager = getContext().getInputManager();
        inputManager.setContext(getContext());
        LabWork newElement = inputManager.readLabWork(argumentUniqueName);

        newElement.setId(id);

        if (targetELement != null) {
            currentCreationDate = targetELement.getDate();
            newElement.setDate(currentCreationDate);
        }

        getContext().getCollectionManager().addElement(newElement);

        if (!wasDeleted) System.out.println("Создан новый элемент");
        else System.out.println("Элемент обновлён");

        return true;
    }
}
