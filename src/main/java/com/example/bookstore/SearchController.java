package com.example.bookstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchController {

    @FXML
    private TextArea resultlist;

    @FXML
    private TextField searchbar;

    @FXML
    private Button searchbtn;

    private MySQLDatabaseConnection dbConnection;

    public SearchController() {
        dbConnection = new MySQLDatabaseConnection();
    }

    @FXML
    void initialize() {
        searchbtn.setOnAction(event -> searchBooks());
    }

    private void searchBooks() {
        String searchTerm = searchbar.getText().trim();
        if (!searchTerm.isEmpty()) {
            dbConnection.connect(); // Ensure the database connection is established
            ObservableList<Book> searchResults = FXCollections.observableArrayList();
            try {
                // Construct the SQL query to search for books by title
                String query = "SELECT * FROM books WHERE quantity > 0 AND title LIKE ?";
                System.out.println("Executing query: " + query); // Debugging statement
                PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
                // Set the search term as a parameter with wildcard characters
                statement.setString(1, "%" + searchTerm + "%");
                ResultSet resultSet = statement.executeQuery();
                // Clear the text area before setting new data
                resultlist.clear();
                while (resultSet.next()) {
                    Book book = new Book(
                            resultSet.getString("isbn"),
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("genre"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("quantity")
                    );
                    // Add each book's details to the text area
                    resultlist.appendText(book.toString() + "\n");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Print the stack trace for any SQL errors
            } finally {
                dbConnection.disconnect(); // Disconnect from the database after retrieving results
            }
        } else {
            // If search term is empty, clear the text area
            resultlist.clear();
        }
    }



    // Helper method to check if a string contains another string (case-insensitive)
    private boolean containsIgnoreCase(String source, String searchStr) {
        return source.toLowerCase().contains(searchStr.toLowerCase());
    }
}
