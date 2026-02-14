package com.andreysankov.itmoprog2sem;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.commands.AbstractCommand;
import com.andreysankov.itmoprog2sem.commands.AddCommand;
import com.andreysankov.itmoprog2sem.commands.ClearCommand;
import com.andreysankov.itmoprog2sem.commands.ExitCommand;
import com.andreysankov.itmoprog2sem.commands.HelpCommand;
import com.andreysankov.itmoprog2sem.commands.InfoCommand;
import com.andreysankov.itmoprog2sem.commands.ShowCommand;
import com.andreysankov.itmoprog2sem.exceptions.FilenameIsEmpty;
import com.andreysankov.itmoprog2sem.managers.CollectionManager;
import com.andreysankov.itmoprog2sem.managers.CommandManager;
import com.andreysankov.itmoprog2sem.managers.Context;
import com.andreysankov.itmoprog2sem.managers.FileManager;
import com.andreysankov.itmoprog2sem.managers.InputManager;

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

