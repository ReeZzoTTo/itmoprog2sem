package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class ShowCommand extends Command{
    public ShowCommand(Context context) {
        super(context);
        this.setName("show");
        this.setDescription(" : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public boolean execute(String[] arguments) {
        if (getContext().getCollectionManager().getCollectionSize() == 0) {
            System.out.println("Коллекция пуста");
        }
        else { 
            System.out.println("Данные коллекции:");
            getContext().getCollectionManager().getCollection().forEach(labWorkObject -> {
                System.out.println(labWorkObject.toString());
            });
        }
        return true;
    }
}
