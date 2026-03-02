package com.andreysankov.itmoprog2sem.managers;

import com.andreysankov.itmoprog2sem.util.LineInput;

public class Context {
    private final FileManager fileManager;
    private final CommandManager commandManager;
    private CollectionManager collectionManager;
    private LineInput lineInput;
    private InputManager inputManager;
    private ErrorManager errorManager;

    public Context(
        FileManager fileManager,
        CommandManager commandManager,
        CollectionManager collectionManager,
        LineInput lineInput,
        InputManager inputManager,
        ErrorManager errorManager
    ) {
        this.fileManager = fileManager;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.lineInput = lineInput;
        this.inputManager = inputManager;
        this.errorManager = errorManager;
    }

    public void setLineInput(LineInput scanner) {
        this.lineInput = scanner;
    }

    public ErrorManager getErrorManager() { return this.errorManager; }
    public FileManager getFileManager() { return this.fileManager; }
    public CommandManager getCommandManager() { return this.commandManager; }
    public CollectionManager getCollectionManager() { return this.collectionManager; }
    public LineInput getLineInput() { return this.lineInput; }
    public InputManager getInputManager() { return this.inputManager; }
}
