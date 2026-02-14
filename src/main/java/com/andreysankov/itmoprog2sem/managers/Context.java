package com.andreysankov.itmoprog2sem.managers;

import java.util.Scanner;

public class Context {
    private final FileManager fileManager;
    private final CommandManager commandManager;
    private CollectionManager collectionManager;
    private Scanner scanner;

    public Context(
        FileManager fileManager,
        CommandManager commandManager,
        CollectionManager collectionManager,
        Scanner scanner
    ) {
        this.fileManager = fileManager;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    public FileManager getFileManager() { return this.fileManager; }
    public CommandManager getCommandManager() { return this.commandManager; }
    public CollectionManager getCollectionManager() { return this.collectionManager; }
    public Scanner getScanner() { return scanner; }
}
