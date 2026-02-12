package com.andreysankov.itmoprog2sem.commands;

public interface Command {
    String getName();
    String getDescription();
    boolean execute(String argument);
}
