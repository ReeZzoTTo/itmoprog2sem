package com.andreysankov.itmoprog2sem.commands;

import com.andreysankov.itmoprog2sem.managers.Context;

public class InfoCommand extends Command {
    public InfoCommand(Context context) {
        super(context);
        this.setName("info");
        this.setDescription(" : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
    }

    @Override
    public boolean execute(String[] arguments) {
        System.out.println("Сведения о коллекции");
        System.out.println("Имя файла коллекции : " + getContext().getFileManager().getFileName());
        System.out.println("Дата инициализации коллекции : " + getContext().getCollectionManager().getInitializationDate());
        System.out.println("Количество элементов коллекции : " + getContext().getCollectionManager().getCollectionSize());

        return true;
    }
}
