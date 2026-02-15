package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.managers.InputManager;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class AddCommand extends AbstractCommand {
    public AddCommand(Context context) {
        super(context);
        this.setName("add");
        this.setDescription(" {element} : добавить новый элемент в коллекцию");
    }

    @Override
    public boolean execute() {
        System.out.println("Добавление элемента в коллекцию");

        InputManager inputManager = getContext().getInputManager();
        inputManager.setContext(getContext());
        LabWork labWork = inputManager.readLabWork(getContext().getCommandManager().getArguments()[1]);
        getContext().getCollectionManager().addElement(labWork);

        System.out.println("Элемент успешно добавлен в коллекцию. Введите show/info");

        return true;
    }
    
}
