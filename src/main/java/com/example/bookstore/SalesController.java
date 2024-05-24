package com.example.bookstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesController {

    @FXML
    private Button back_btn;

    @FXML
    private ListView<String> soldlist;

    @FXML
    private TextField total_txt;

    private MySQLDatabaseConnection dbConnection;

    public SalesController() {
        dbConnection = new MySQLDatabaseConnection();
        dbConnection.connect();
    }

    @FXML
    private void initialize() {
        loadSoldItems();
        calculateAndDisplayTotalRevenue();
        back_btn.setOnAction(this::handleBackButtonAction);
    }

    private void loadSoldItems() {
        ObservableList<String> soldItems = FXCollections.observableArrayList();

        String sql = "SELECT * FROM soldbooks";

        try {
            var preparedStatement = dbConnection.getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String item = rs.getString("title") + " by " + rs.getString("author") +
                        " - Genre: " + rs.getString("genre") +
                        ", Price: P" + rs.getDouble("price") +
                        ", Quantity: " + rs.getInt("quantity");
                soldItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        soldlist.setItems(soldItems);
    }

    private void calculateAndDisplayTotalRevenue() {
        double totalRevenue = 0.0;

        String sql = "SELECT price, quantity FROM soldbooks";

        try {
            var preparedStatement = dbConnection.getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                totalRevenue += price * quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        total_txt.setText(String.format("%.2f", totalRevenue));
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
