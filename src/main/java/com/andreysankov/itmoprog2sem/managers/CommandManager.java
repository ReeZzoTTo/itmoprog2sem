package com.andreysankov.itmoprog2sem.managers;

import java.util.HashMap;
import java.util.Map;
import com.andreysankov.itmoprog2sem.commands.AbstractCommand;

public class CommandManager {
    private Map<String, AbstractCommand> commandList = new HashMap<>();

    public void registerCommand(
        String commandName, 
        AbstractCommand command
    ) {
        this.commandList.put(commandName, command);
    }

    public Map<String, AbstractCommand> getCommandList() { return this.commandList; }
}
