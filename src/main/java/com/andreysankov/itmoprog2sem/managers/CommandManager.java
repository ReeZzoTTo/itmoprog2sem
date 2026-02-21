package com.andreysankov.itmoprog2sem.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.andreysankov.itmoprog2sem.commands.Command;

public class CommandManager {
    private Map<String, Command> commandList = new HashMap<>();
    private String[] arguments;
    private List<String> history = new ArrayList<>();
    private Context context;

    public void registerCommand(
        String commandName, 
        Command command
    ) {
        this.commandList.put(commandName, command);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addToHistory(String commandName) {
        if (this.history.size() == 10) {
            this.history.removeFirst();
        }
        history.add(commandName);
    }

    public String getArgument(String[] arguments, int index, String message) {
        String argument;
        try {
            argument = arguments[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            context.getErrorManager().setException(new ArrayIndexOutOfBoundsException(message));
            argument = null;
        }
        return argument;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public List<String> getHistory() { return this.history; }
    public String[] getArguments() { return this.arguments; }
    public Map<String, Command> getCommandList() { return this.commandList; }
}
