package com.andreysankov.itmoprog2sem;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.commands.*;
import com.andreysankov.itmoprog2sem.exceptions.FilenameIsEmpty;
import com.andreysankov.itmoprog2sem.managers.*;

// TODO : разобраться с Date

// java -Dfile.encoding=UTF-8 -jar target/itmoprog2sem-1.0-Lab5.jar


public class App {
    public static void main( String[] args ) {
        try {
            String targetFilePath = System.getenv("LABWORK_FILE");

            if (targetFilePath == null) 
                throw new FilenameIsEmpty("Пустое имя файла. Вероятно ошибка с переменной окружения");
        } catch (FilenameIsEmpty e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        Context context = new Context(
            new FileManager(System.getenv("LABWORK_FILE")),
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
        commandManager.registerCommand("history", new HistoryCommand(context));
        commandManager.registerCommand("average_of_personal_qualities_maximum", new AverageOfPersonalQualititesMaximumCommand(context));
        
        System.out.println("Программа для управления колекцией");
        System.out.println("Чтение файла : " + context.getFileManager().getFileName());
        context.getCollectionManager().setCollection(context.getFileManager().readFile());
        System.out.println("Чтение файла завершено.\nДля просмотра данных коллекции введите -> show.\nВведите help для списка команд");
        
        context.getCollectionManager().setInitializationDate(context);

        while (true) {
            String input = context.getScanner().nextLine();
            String[] inputSplit = input.split(" ");

            AbstractCommand command = context.getCommandManager().getCommandList().get(inputSplit[0]);

            if (command != null) { 
                context.getCommandManager().setArguments(inputSplit);
                command.execute(); 
                context.getCommandManager().addToHistory(inputSplit[0]);
            }
            else { System.out.println("Неизвестная команда : Введите help"); }            
        }
    }
}

