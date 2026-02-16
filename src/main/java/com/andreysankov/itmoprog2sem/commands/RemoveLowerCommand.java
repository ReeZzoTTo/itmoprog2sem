package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(Context context) {
        super(context);
        this.setName("remove_lower");
        this.setDescription(" {element} : удалить из коллекции все элементы, меньшие, чем заданный");
    }

    @Override
    public boolean execute() {
        String argumentUniqueName = getContext().getCommandManager().getArguments()[1];
        if (getContext().getCollectionManager().getElementsUniqueName().contains(argumentUniqueName)) {
            System.out.println("Элемент найден");

            int removeElementsCount = 0;
            LabWork currentElement = getContext().getCollectionManager().getElementByUniqueName(argumentUniqueName);

            //! Проблема итератора при удалении элемента
            //TODO : решить йоу

            for (LabWork element : getContext().getCollectionManager().getCollection()) {
                if (element.getMinimalPoint() < currentElement.getMinimalPoint()) {
                    getContext().getCollectionManager().deleteElementByID(element.getId());
                    System.out.println(element.getUniqueName() + " успешно удалён");
                    removeElementsCount++;
                }
            }
            System.out.println("Удалено элементов : " + removeElementsCount);
        } else {
            System.out.println("Элемент не найден. Создайте его : add");
        }
        
        return true;
    } 
}
