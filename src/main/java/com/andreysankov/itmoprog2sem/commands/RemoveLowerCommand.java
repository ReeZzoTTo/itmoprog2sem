package com.andreysankov.itmoprog2sem.commands;

import java.util.Iterator;

import com.andreysankov.itmoprog2sem.exceptions.AppException;
import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.models.LabWork;

public class RemoveLowerCommand extends Command {
    public RemoveLowerCommand(Context context) {
        super(context);
        this.setName("remove_lower");
        this.setDescription(" {element} : удалить из коллекции все элементы, меньшие, чем заданный");
    }

    @Override
    public boolean execute(String[] arguments) {
        String argumentUniqueName = getContext().getCommandManager().getArgument(arguments, 1, "Укажите именной идентификатор элемента\n(Команда remove_lower требует аргумент)");
        if (argumentUniqueName == null) return false;

        if (getContext().getCollectionManager().getElementsUniqueName().contains(argumentUniqueName)) {
            System.out.println("Элемент найден");

            int removeElementsCount = 0;
            LabWork currentElement = getContext().getCollectionManager().getElementByUniqueName(argumentUniqueName);

            Iterator<LabWork> iterator = getContext().getCollectionManager().getIterator();

            while (iterator.hasNext()) {
                LabWork element = iterator.next();

                if (element.getMinimalPoint() < currentElement.getMinimalPoint()) {
                    iterator.remove();
                    System.out.println(element.getUniqueName() + " успешно удалён");
                    removeElementsCount++;
                }
            }
            System.out.println("Удалено элементов : " + removeElementsCount);
            
            return true;
        }
        
        getContext().getErrorManager().setException(new AppException("Элемент не найден. Создайте его : add"));
        return false;
    } 
}
