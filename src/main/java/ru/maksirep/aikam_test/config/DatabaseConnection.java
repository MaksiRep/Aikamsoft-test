package ru.maksirep.aikam_test.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL;
    private static final String USER_NAME;
    private static final String PASSWORD;

    static {
        ConfigLoader configLoader = new ConfigLoader("application.properties");
        URL = configLoader.getProperty("db.url");
        USER_NAME = configLoader.getProperty("db.username");
        PASSWORD = configLoader.getProperty("db.password");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}