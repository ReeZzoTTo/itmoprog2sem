package com.andreysankov.itmoprog2sem;

import java.util.Scanner;

import com.andreysankov.itmoprog2sem.commands.HelpCommand;
import com.andreysankov.itmoprog2sem.exceptions.FilenameIsEmpty;
import com.andreysankov.itmoprog2sem.managers.CommandManager;
import com.andreysankov.itmoprog2sem.managers.FileManager;

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

        Scanner scanner = new Scanner(System.in);
        FileManager fileManager = new FileManager(args[0]);
        CommandManager commandManager = new CommandManager();

        commandManager.registerCommand("help", new HelpCommand());

        System.out.println(fileManager.getFileName());

        

        while (true) {
            String input = scanner.nextLine();

            switch(input) {
                case "exit", "exit()": {
                    scanner.close();
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
            }

            System.out.println("Input " + input);
        }
    }
}

