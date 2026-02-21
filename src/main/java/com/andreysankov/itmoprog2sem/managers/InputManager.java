package com.andreysankov.itmoprog2sem.managers;

import java.util.HashSet;
import java.util.Scanner;
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

    public void readConsole(Scanner scanner) {
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            String[] inputSplit = input.split(" ");

            Command command = context.getCommandManager().getCommandList().get(inputSplit[0].trim());

            if (command != null) { 
                
                if (!command.execute(inputSplit)) {
                    context.getErrorManager().executeError();
                } 
                context.getCommandManager().addToHistory(inputSplit[0]);
            }
            else { System.out.println("Неизвестная команда : Введите help"); }            
        }
    }

    public LabWork readLabWork(String uniqueName) {
        InputLabWork inputLabWork = new InputLabWork(context);
        
        return new LabWork(
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
    }
}
