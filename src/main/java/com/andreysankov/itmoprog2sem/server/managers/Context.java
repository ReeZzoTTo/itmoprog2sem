package com.andreysankov.itmoprog2sem.server.managers;

import com.andreysankov.itmoprog2sem.server.database.DatabaseManager;
import com.andreysankov.itmoprog2sem.server.database.UserRepository;

public class Context {
    private final DatabaseManager databaseManager;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final UserRepository userRepository;

    public Context(
        CommandManager commandManager,
        CollectionManager collectionManager,
        DatabaseManager databaseManager
    ) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
        this.userRepository = new UserRepository(databaseManager);
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
}
