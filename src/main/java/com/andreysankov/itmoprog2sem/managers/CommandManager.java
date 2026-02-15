package com.andreysankov.itmoprog2sem.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.andreysankov.itmoprog2sem.commands.AbstractCommand;

public class CommandManager {
    private Map<String, AbstractCommand> commandList = new HashMap<>();
    private String[] arguments;
    private List<String> history = new ArrayList<>();

    public void registerCommand(
        String commandName, 
        AbstractCommand command
    ) {
        this.commandList.put(commandName, command);
    }

    public void addToHistory(String commandName) {
        if (this.history.size() == 10) {
            this.history.removeFirst();
        }
        history.add(commandName);
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public List<String> getHistory() { return this.history; }
    public String[] getArguments() { return this.arguments; }
    public Map<String, AbstractCommand> getCommandList() { return this.commandList; }
}
