package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.exceptions.AppException;
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
        String argumentUniqueName;
        try {
            argumentUniqueName = getContext().getCommandManager().getArguments()[1];
            if (getContext().getCollectionManager().getElementsUniqueName().contains(argumentUniqueName)) {
                getContext().getErrorManager().setException(new AppException("Элемент с таким именем уже существует"));
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            getContext().getErrorManager().setException(new ArrayIndexOutOfBoundsException("Укажите именной идентификатор элементу\n(Команда add требует аргумент)"));
            return false;
        }
        
        System.out.println("Добавление элемента в коллекцию");

        InputManager inputManager = getContext().getInputManager();
        inputManager.setContext(getContext());
        LabWork labWork = inputManager.readLabWork(argumentUniqueName);
        getContext().getCollectionManager().addElement(labWork);

        System.out.println("Элемент успешно добавлен в коллекцию. Введите show/info");

        return true;
    }
    
}
