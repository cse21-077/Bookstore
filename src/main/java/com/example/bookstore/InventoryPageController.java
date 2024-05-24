package com.example.bookstore;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class  InventoryPageController {

    @FXML
    private ListView<Book> inventoryList;

    @FXML
    private Button editBtn;

    @FXML
    private Button deleteBtn;

    // Inject the database connection
    private MySQLDatabaseConnection dbConnection;

    public InventoryPageController() {
        dbConnection = new MySQLDatabaseConnection();
    }

    @FXML
    public void initialize() {
        // Load inventory data on initialization
        loadInventory();
    }

    // Method to load inventory data into the ListView
    private void loadInventory() {
        dbConnection.connect();
        inventoryList.getItems().clear(); // Clear the list before loading new data
        // Fetch all books from the database
        ResultSet resultSet = dbConnection.executeQuery("SELECT * FROM books");
        try {
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString("isbn"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity")
                );
                inventoryList.getItems().add(book); // Add each book to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.disconnect();
        }
    }

    // Method to handle editing a book
    @FXML
    private void editBook() {
        Book selectedBook = inventoryList.getSelectionModel().getSelectedItem();
        // Implement editing functionality (e.g., open a new window for editing)
        System.out.println("Edit book: " + selectedBook);
    }

    // Method to handle deleting a book
    @FXML
    private void deleteBook() {
        Book selectedBook = inventoryList.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            dbConnection.connect();
            String isbn = selectedBook.getIsbn();
            // Execute the delete query
            dbConnection.executeUpdate("DELETE FROM books WHERE isbn = '" + isbn + "'");
            dbConnection.disconnect();
            // Reload inventory after deletion
            loadInventory();
        }
    }
}
