package com.example.bookstore;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryController {

    @FXML
    private Button back_btn;

    @FXML
    private ListView<String> inventorylist;

    private MySQLDatabaseConnection dbConnection;

    public InventoryController() {
        dbConnection = new MySQLDatabaseConnection();
        dbConnection.connect();
    }

    @FXML
    private void initialize() {
        loadInventory();
        back_btn.setOnAction(this::handleBackButtonAction);
    }

    private void loadInventory() {
        ObservableList<String> inventoryItems = FXCollections.observableArrayList();

        String sql = "SELECT * FROM books WHERE quantity > 0";

        try {
            ResultSet rs = dbConnection.executeQuery(sql);
            while (rs.next()) {
                String item = rs.getString("title") + " by " + rs.getString("author") +
                        " - Genre: " + rs.getString("genre") +
                        ", Price: $" + rs.getDouble("price") +
                        ", Quantity: " + rs.getInt("quantity");
                inventoryItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inventorylist.setItems(inventoryItems);
    }

    private void handleBackButtonAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
