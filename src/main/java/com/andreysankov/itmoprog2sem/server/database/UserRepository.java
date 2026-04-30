package com.andreysankov.itmoprog2sem.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private final DatabaseManager databaseManager;

    public UserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean register(String login, String password) throws SQLException {
        String passwordHash = Md5Util.hash(password);

        String sqlQuery = """
            INSERT INTO users (login, password_hash)
            VALUES (?, ?)
        """;

        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, passwordHash);

            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean checkCredentials(String login, String password) throws SQLException {
        String passwordHash = Md5Util.hash(password);

        String sqlQuery = """
            SELECT COUNT(*) AS user_count       
            FROM users
            WHERE login = ? AND password_hash = ?
        """;

        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, passwordHash);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_count") == 1;
                }

                return false;
            }
        }
    }

    public boolean existsByLogin(String login) throws SQLException {
        String sqlQuery = """
            SELECT COUNT(*) AS login_count
            FROM users 
            WHERE login = ?        
        """;

        try (
            Connection connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("login_count") == 1;
                } 
                
                return false;
            }
        }
    }
}
