package com.andreysankov.itmoprog2sem.managers;

import java.util.Scanner;

public class Context {
    private final FileManager fileManager;
    private final CommandManager commandManager;
    private CollectionManager collectionManager;
    private Scanner scanner;
    private InputManager inputManager;
    private ErrorManager errorManager;

    public Context(
        FileManager fileManager,
        CommandManager commandManager,
        CollectionManager collectionManager,
        Scanner scanner,
        InputManager inputManager,
        ErrorManager errorManager
    ) {
        this.fileManager = fileManager;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.scanner = scanner;
        this.inputManager = inputManager;
        this.errorManager = errorManager;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public ErrorManager getErrorManager() { return this.errorManager; }
    public FileManager getFileManager() { return this.fileManager; }
    public CommandManager getCommandManager() { return this.commandManager; }
    public CollectionManager getCollectionManager() { return this.collectionManager; }
    public Scanner getScanner() { return this.scanner; }
    public InputManager getInputManager() { return this.inputManager; }
}
