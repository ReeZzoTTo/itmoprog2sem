package com.andreysankov.itmoprog2sem.server.managers;

import com.andreysankov.itmoprog2sem.server.database.DatabaseManager;
import com.andreysankov.itmoprog2sem.server.database.LabWorkRepository;
import com.andreysankov.itmoprog2sem.server.database.UserRepository;

public class Context {
    private final DatabaseManager databaseManager;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final UserRepository userRepository;
    private final LabWorkRepository labWorkRepository;

    public Context(
        CommandManager commandManager,
        CollectionManager collectionManager,
        DatabaseManager databaseManager
    ) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
        this.userRepository = new UserRepository(databaseManager);
        this.labWorkRepository = new LabWorkRepository(databaseManager);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public LabWorkRepository getLabWorkRepository() {
        return labWorkRepository;
    }
}
