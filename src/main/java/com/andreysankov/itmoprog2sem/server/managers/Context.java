package com.andreysankov.itmoprog2sem.server.managers;


public class Context {
    private final FileManager fileManager;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;

    public Context(
        FileManager fileManager,
        CommandManager commandManager,
        CollectionManager collectionManager
    ) {
        this.fileManager = fileManager;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}