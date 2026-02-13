package com.andreysankov.itmoprog2sem.commands;

public class HelpCommand extends AbstractCommand {
    String name = "help";
    String description = "вывести справку по доступным командам";
    
    @Override
    public boolean execute(String argument) {
        return true;
    }

}
