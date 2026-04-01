package com.andreysankov.itmoprog2sem.server.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andreysankov.itmoprog2sem.common.commands.Command;

public class CommandManager {
    private Map<String, Command> commandList = new HashMap<>();
    private List<String> history = new ArrayList<>();
    
    public void registerCommand(
        String commandName, 
        Command command
    ) {
        this.commandList.put(commandName, command);
    }

    public void addToHistory(String commandName) {
        if (this.history.size() == 10) {
            this.history.removeFirst();
        }
        history.add(commandName);
    }

    public List<String> getHistory() { return this.history; }
    public Map<String, Command> getCommandList() { return this.commandList; }
}
