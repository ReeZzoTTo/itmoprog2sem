package com.andreysankov.itmoprog2sem.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DEFAULT_HOST = "pg";
    private static final String DEFAULT_PORT = "5432";
    private static final String DEFAULT_DATABASE = "studs";

    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager() {
        String host = getEnvOrDefault("DB_HOST", DEFAULT_HOST);
        String port = getEnvOrDefault("DB_PORT", DEFAULT_PORT);
        String database = getEnvOrDefault("DB_NAME", DEFAULT_DATABASE);
    
        this.user = getEnvOrDefault("DB_USER", System.getProperty("user.name"));
        this.password = getEnvOrDefault("DB_PASSWORD", "");
        this.url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void initialize() throws SQLException {
        try (
            Connection conn = getConnection();
            Statement statement = conn.createStatement()
        ) {
            statement.executeUpdate("CREATE SEQUENCE IF NOT EXISTS labwork_id_seq START 1");

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    login TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL
                )
            """);

             statement.executeUpdate("""
                DO $$
                BEGIN
                    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'difficulty_type') THEN
                        CREATE TYPE difficulty_type AS ENUM (
                            'VERY_EASY',
                            'HARD',
                            'IMPOSSIBLE',
                            'INSANE',
                            'TERRIBLE'
                        );
                    END IF;
                END
                $$;
            """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS labworks (
                    id BIGINT PRIMARY KEY DEFAULT nextval('labwork_id_seq'),
                    name TEXT NOT NULL,
                    coordinates_x BIGINT NOT NULL,
                    coordinates_y INTEGER NOT NULL,
                    creation_date TIMESTAMP NOT NULL,
                    minimal_point INTEGER NOT NULL,
                    personal_qualities_maximum DOUBLE PRECISION,
                    difficulty difficulty_type NOT NULL,
                    discipline_name TEXT,
                    discipline_lecture_hours BIGINT,
                    discipline_labs_count INTEGER,
                    owner_login TEXT NOT NULL,

                    FOREIGN KEY (owner_login) REFERENCES users(login) 
                    ON DELETE CASCADE
                )   
            """);
        }
    }

    private String getEnvOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

}
