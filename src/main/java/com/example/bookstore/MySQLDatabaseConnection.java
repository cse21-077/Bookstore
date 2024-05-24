package com.example.bookstore;

import java.sql.*;

// Implementation of the DatabaseConnection interface
public class MySQLDatabaseConnection implements DatabaseConnection {
    // JDBC URL for connecting to MySQL database (replace with your database URL)
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bookstore";
    // Database credentials (replace with your credentials)
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    @Override
    public void connect() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to the database
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet executeQuery(String sql) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    @Override
    public void executeUpdate(String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getter method for the Connection object
    public Connection getConnection() {
        return connection;
    }

    // Method to check user type and open appropriate page
    public void openPage(String userType) {
        if ("admin".equals(userType)) {
            // Open admin page (replace with your implementation)
            // For JavaFX, you would typically load a new FXML file here
            System.out.println("Opening admin page");
        } else if ("customer".equals(userType)) {
            // Open customer page (replace with your implementation)
            // For JavaFX, you would typically load a new FXML file here
            System.out.println("Opening customer page");
        } else {
            System.out.println("Invalid user type");
        }
    }

    public static void main(String[] args) {
        // Example usage
        MySQLDatabaseConnection dbConnection = new MySQLDatabaseConnection();
        dbConnection.connect();


    }
}
