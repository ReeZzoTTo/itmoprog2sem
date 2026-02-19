package com.andreysankov.itmoprog2sem;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.andreysankov.itmoprog2sem.commands.*;
import com.andreysankov.itmoprog2sem.exceptions.AppException;
import com.andreysankov.itmoprog2sem.managers.*;


public class App {
    public static void main( String[] args ) {
        ErrorManager errorManager = new ErrorManager();
        String envName = "LABWORK_FILE";

        try {
            if (args.length != 0) envName = args[0];

            String targetFilePath = System.getenv(envName.toUpperCase());

            if (targetFilePath == null) 
                throw new AppException("Пустое имя файла. Вероятно ошибка с переменной окружения");
        } catch (AppException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        Context context = new Context(
            new FileManager(System.getenv(envName.toUpperCase())),
            new CommandManager(),
            new CollectionManager(),
            new Scanner(System.in, StandardCharsets.UTF_8),
            new InputManager(),
            errorManager
        );

        CommandManager commandManager = context.getCommandManager();
        commandManager.setContext(context);

        commandManager.registerCommand("help", new HelpCommand(context));
        commandManager.registerCommand("info", new InfoCommand(context));
        commandManager.registerCommand("show", new ShowCommand(context));
        commandManager.registerCommand("add", new AddCommand(context));
        commandManager.registerCommand("update", new UpdateIdCommand(context));
        commandManager.registerCommand("remove_by_id", new RemoveByIdCommand(context));
        commandManager.registerCommand("save", new SaveCommand(context));
        commandManager.registerCommand("execute_script", new ExecuteScriptCommand(context));
        commandManager.registerCommand("clear", new ClearCommand(context));
        commandManager.registerCommand("exit", new ExitCommand(context));
        commandManager.registerCommand("add_if_min", new AddIfMinCommand(context));
        commandManager.registerCommand("remove_lower", new RemoveLowerCommand(context));
        commandManager.registerCommand("history", new HistoryCommand(context));
        commandManager.registerCommand("average_of_personal_qualities_maximum", new AverageOfPersonalQualititesMaximumCommand(context));
        commandManager.registerCommand("group_counting_by_creation_date", new GroupCountingByCreationDateCommand(context));
        commandManager.registerCommand("print_field_descending_discipline", new PrintFieldDescendingDisciplineCommand(context));
        
        System.out.println("Программа для управления колекцией");
        System.out.println("Чтение файла : " + context.getFileManager().getFileName());

        context.getCollectionManager().setCollection(context.getFileManager().readFile());
        context.getCollectionManager().setDisciplineMap();

        System.out.println("Чтение файла завершено.\nДля просмотра данных коллекции введите -> show.\nВведите help для списка команд");
        
        context.getCollectionManager().setInitializationDate(context);

        Scanner scanner = context.getScanner();

        context.getInputManager().setContext(context);
        context.getInputManager().readConsole(scanner);
    }
}

