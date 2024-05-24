package com.example.bookstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookController {

    @FXML
    private ListView<Book> availablelist;

    @FXML
    private Button searchnav;

    @FXML
    private Button purchasehistory_btn;

    @FXML
    private ListView<Book> topsellerslist;

    private MySQLDatabaseConnection dbConnection;
    private int currentUserId; // User ID of the logged-in user

    public BookController() {
        dbConnection = new MySQLDatabaseConnection();
    }

    @FXML
    void initialize() {
        loadAvailableBooks();
        loadTopSellers();

        searchnav.setOnAction(event -> openSearchPage());

        purchasehistory_btn.setOnAction(event -> openPurchaseHistory());

        availablelist.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Book selectedBook = availablelist.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    openDetailsPage(selectedBook);
                }
            }
        });
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    private void loadAvailableBooks() {
        dbConnection.connect();
        ObservableList<Book> availableBooks = FXCollections.observableArrayList();
        ResultSet resultSet = dbConnection.executeQuery("SELECT * FROM books WHERE quantity > 0");
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
                availableBooks.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.disconnect();
        }
        availablelist.setItems(availableBooks);
    }

    private void loadTopSellers() {
        dbConnection.connect();
        ObservableList<Book> topSellers = FXCollections.observableArrayList();
        ResultSet resultSet = dbConnection.executeQuery("SELECT * FROM books ORDER BY quantity DESC LIMIT 5");
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
                topSellers.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.disconnect();
        }
        topsellerslist.setItems(topSellers);
    }

    private void openDetailsPage(Book selectedBook) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detailspage.fxml"));
            Parent root = loader.load();

            DetailsController detailsController = loader.getController();
            detailsController.setSelectedBook(selectedBook);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Book Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSearchPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Search");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openPurchaseHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("purchasehistory.fxml"));
            Parent root = loader.load();

            PurchaseHistoryController purchaseHistoryController = loader.getController();
            purchaseHistoryController.setCurrentUserId(currentUserId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Purchase History");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
