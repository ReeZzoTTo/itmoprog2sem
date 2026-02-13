package com.andreysankov.itmoprog2sem.commands;

public interface Command {
    public String getName();
    public String getDescription();
    public boolean execute(String argument);
}
