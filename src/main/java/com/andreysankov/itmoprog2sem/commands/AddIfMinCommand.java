package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.managers.InputManager;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class AddIfMinCommand extends AbstractCommand {
    public AddIfMinCommand(Context context) {
        super(context);
        this.setName("add_if_min");
        this.setDescription(" {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
    }

    @Override
    public boolean execute() {
        String argumentUniqueName;
        try {
            argumentUniqueName = getContext().getCommandManager().getArguments()[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Укажите именной идентификатор элементу\n(Команда add_if_min требует аргумент)");
            return true;
        }

        int minimum = this.getMinOfMinimalPoint();

        InputManager inputManager = getContext().getInputManager();
        inputManager.setContext(getContext());

        LabWork labWork = inputManager.readLabWork(argumentUniqueName);
        
        if (labWork.getMinimalPoint() < minimum) {
            getContext().getCollectionManager().addElement(labWork);
            System.out.println("Элемент " + labWork.getUniqueName() + " успешно добавлен в коллекцию");
        } else {
            System.out.println("Элемент с указанным значением minimalPoint=" + labWork.getMinimalPoint() + " не является наименьшим\nЭлемент не был добавлен");
        }

        return true;
    }

    public int getMinOfMinimalPoint() {
        int minimum = -1;

        for (LabWork element : getContext().getCollectionManager().getCollection()) {
            int currentMinimalPoint = element.getMinimalPoint();
            
            if (minimum == -1) minimum = currentMinimalPoint;
            else {
                if (currentMinimalPoint < minimum) minimum = currentMinimalPoint;
            }
        }

        return minimum;
    }
}
