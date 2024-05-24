package com.example.bookstore;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DetailsController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Button buyButton;

    private Book selectedBook;
    private MySQLDatabaseConnection dbConnection;
    private int currentUserId; // User ID of the logged-in user

    public DetailsController() {
        dbConnection = new MySQLDatabaseConnection();
        dbConnection.connect();
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
        updateLabels();
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        System.out.println("DetailsController: Current User ID set to: " + userId); // Debugging statement
    }

    private void updateLabels() {
        if (selectedBook != null) {
            titleLabel.setText(selectedBook.getTitle());
            authorLabel.setText(selectedBook.getAuthor());
            genreLabel.setText(selectedBook.getGenre());
            priceLabel.setText(String.valueOf(selectedBook.getPrice()));
            quantityLabel.setText(String.valueOf(selectedBook.getQuantity()));
        }
    }

    @FXML
    void buyBook() {
        System.out.println("Buy button clicked."); // Debugging statement
        if (selectedBook != null) {
            try {
                // Check if the book is available
                if (selectedBook.getQuantity() > 0) {
                    // Decrease the quantity of the book
                    int newQuantity = selectedBook.getQuantity() - 1;
                    selectedBook.setQuantity(newQuantity);
                    updateBookQuantity(selectedBook.getIsbn(), newQuantity);

                    // Add to soldbooks table
                    addSoldBook(selectedBook);

                    // Update the quantity label
                    quantityLabel.setText(String.valueOf(newQuantity));

                    // Success message
                    System.out.println("Successfully bought book: " + selectedBook.getTitle());
                } else {
                    // Handle case when the book is out of stock
                    System.out.println("Book is out of stock: " + selectedBook.getTitle());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBookQuantity(String isbn, int quantity) throws SQLException {
        String sql = "UPDATE books SET quantity = ? WHERE isbn = ?";
        try (var preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, isbn);
            preparedStatement.executeUpdate();
        }
    }

    private void addSoldBook(Book book) throws SQLException {
        String sql = "INSERT INTO soldbooks (isbn, title, author, genre, price, quantity, sold_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setString(4, book.getGenre());
            preparedStatement.setDouble(5, book.getPrice());
            preparedStatement.setInt(6, 1); // Assuming 1 book bought at a time
            preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis())); // current date and time
            preparedStatement.executeUpdate();
        }
    }

}
