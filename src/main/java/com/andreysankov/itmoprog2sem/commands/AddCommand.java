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
        InputManager inputManager = getContext().getInputManager();
        inputManager.setContext(getContext());
        LabWork labWork = inputManager.readLabWork();
        System.out.println(labWork.getName());
        getContext().getCollectionManager().addElement(labWork);

        return true;
    }
    
}
