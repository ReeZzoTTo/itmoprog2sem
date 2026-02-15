package com.andreysankov.itmoprog2sem;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.commands.*;
import com.andreysankov.itmoprog2sem.exceptions.FilenameIsEmpty;
import com.andreysankov.itmoprog2sem.managers.*;

public class App {
    public static void main( String[] args ) {
        try {
            if (args.length == 0) {
                throw new FilenameIsEmpty("Необходимо ввести имя файла");
            }
        } catch (FilenameIsEmpty exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }

        Context context = new Context(
            new FileManager(args[0]),
            new CommandManager(),
            new CollectionManager(),
            new Scanner(System.in, StandardCharsets.UTF_8),
            new InputManager()
        );
        CommandManager commandManager = context.getCommandManager();

        commandManager.registerCommand("help", new HelpCommand(context));
        commandManager.registerCommand("info", new InfoCommand(context));
        commandManager.registerCommand("show", new ShowCommand(context));
        commandManager.registerCommand("add", new AddCommand(context));
        commandManager.registerCommand("update", new UpdateIdCommand(context));
        commandManager.registerCommand("remove_by_id", new RemoveByIdCommand(context));
        commandManager.registerCommand("save", new SaveCommand(context));
        commandManager.registerCommand("clear", new ClearCommand(context));
        commandManager.registerCommand("exit", new ExitCommand(context));
    
        while (true) {
            String input = context.getScanner().nextLine();
            String[] inputSplit = input.split(" ");

            AbstractCommand command = context.getCommandManager().getCommandList().get(inputSplit[0]);

            if (command != null) { 
                context.getCommandManager().setArguments(inputSplit);
                command.execute(); 
            }
            else { System.out.println("Неизвестная команда : Введите help"); }            
        }
    }
}

