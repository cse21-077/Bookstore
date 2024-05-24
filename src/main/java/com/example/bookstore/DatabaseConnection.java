package com.example.bookstore;

import java.sql.*;

// Interface for database operations
public interface DatabaseConnection {
    // Connect to the database
    void connect();
    // Disconnect from the database
    void disconnect();
    // Execute a query and return the result set
    ResultSet executeQuery(String sql);
    // Execute an update operation (e.g., INSERT, UPDATE, DELETE)
    void executeUpdate(String sql);
}

