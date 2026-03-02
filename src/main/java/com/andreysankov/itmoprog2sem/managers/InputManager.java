package com.andreysankov.itmoprog2sem.managers;

import java.util.HashSet;
import java.util.Set;

import com.andreysankov.itmoprog2sem.commands.Command;
import com.andreysankov.itmoprog2sem.models.*;
import com.andreysankov.itmoprog2sem.util.InputLabWork;

public class InputManager {
    private Context context;
    private Set<String> fileScriptSet = new HashSet<>();

    public void setContext(Context context) {
        this.context = context;
    }

    public void addFileScriptToSet(String filename) {
        this.fileScriptSet.add(filename);
    }

    public Set<String> getFileScriptsSet() {
        return this.fileScriptSet;
    }

    public void readScript() {
        while (true) {
            String line = context.getLineInput().readLine("");
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] inputSplit = line.split("\\s+");
            
            Command command = context.getCommandManager().getCommandList().get(inputSplit[0]);

            if (command != null) {
                if (!command.execute(inputSplit)) {
                    context.getErrorManager().executeError();
                }
            } 
        }
    }
 

    public void readConsoleInteractive() {
        while (true) {
            String input = context.getLineInput().readLine("> ");
            if (input == null) continue;

            input = input.trim();
            if (input.isEmpty()) continue;

            String[] inputSplit = input.split("\\s+");
            
            Command command = context.getCommandManager().getCommandList().get(inputSplit[0]);

            if (command != null) {
                if (!command.execute(inputSplit)) {
                    context.getErrorManager().executeError();
                }
                context.getCommandManager().addToHistory(inputSplit[0]);
            } else {
                System.out.println("Неизвестная команда : Введите help");
            }
        }
    }

    public LabWork readLabWork(String uniqueName) {
        InputLabWork inputLabWork = new InputLabWork(context);
        
        inputLabWork.disableHistory();

        LabWork labwork = new LabWork(
            context.getCollectionManager().generateId(),
            inputLabWork.inputName("Укажите название:"),
            inputLabWork.inputCoordinates(),
            inputLabWork.inputCreationDate(),
            inputLabWork.inputMinimalPoint("Укажите минимальное число очков:"),
            inputLabWork.inputPersonalQualitiesMaximum(),
            inputLabWork.inputDifficulty(),
            inputLabWork.inputDiscipline(),
            uniqueName
        );

        inputLabWork.enableHistory();
        
        return labwork;
    }
}
