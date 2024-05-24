package com.example.bookstore;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddBookController {

    @FXML
    private Button addbook_btn;

    @FXML
    private TextField author_txt;

    @FXML
    private TextField genre_txt;

    @FXML
    private TextField isbn_txt;

    @FXML
    private TextField price_txt;

    @FXML
    private TextField quantity_txt;

    @FXML
    private TextField title_txt;

    private MySQLDatabaseConnection dbConnection;

    public AddBookController() {
        dbConnection = new MySQLDatabaseConnection();
        dbConnection.connect();
    }

    @FXML
    private void initialize() {
        addbook_btn.setOnAction(event -> addBook());
    }

    private void addBook() {
        // Get the text from the text fields
        String isbn = isbn_txt.getText();
        String title = title_txt.getText();
        String author = author_txt.getText();
        String genre = genre_txt.getText();
        double price;
        int quantity;

        try {
            price = Double.parseDouble(price_txt.getText());
            quantity = Integer.parseInt(quantity_txt.getText());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid input", "Price and Quantity must be numeric.");
            return;
        }

        // Create a new Book object
        Book book = new Book(isbn, title, author, genre, price, quantity);

        // Create the SQL insert statement
        String sql = "INSERT INTO books (isbn, title, author, genre, price, quantity) VALUES ('" +
                book.getIsbn() + "', '" + book.getTitle() + "', '" + book.getAuthor() + "', '" +
                book.getGenre() + "', " + book.getPrice() + ", " + book.getQuantity() + ")";

        // Execute the SQL statement
        try {
            dbConnection.executeUpdate(sql);
            showAlert(AlertType.INFORMATION, "Success", "Book added successfully.");
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Could not add book to the database.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        isbn_txt.clear();
        title_txt.clear();
        author_txt.clear();
        genre_txt.clear();
        price_txt.clear();
        quantity_txt.clear();
    }
}
