package com.example.bookstore;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseHistoryController {

    @FXML
    private ListView<String> historylist;

    private MySQLDatabaseConnection dbConnection;
    private int currentUserId;

    public PurchaseHistoryController() {
        dbConnection = new MySQLDatabaseConnection();
    }

    @FXML
    private void initialize() {
        loadPurchaseHistory();
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
        loadPurchaseHistory();
    }

    private void loadPurchaseHistory() {
        if (currentUserId == 0) return;

        ObservableList<String> purchaseHistory = FXCollections.observableArrayList();

        String sql = "SELECT * FROM soldbooks WHERE user_id = ?";

        try {
            var preparedStatement = dbConnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, currentUserId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String item = rs.getString("title") + " by " + rs.getString("author") +
                        " - Genre: " + rs.getString("genre") +
                        ", Price: $" + rs.getDouble("price") +
                        ", Quantity: " + rs.getInt("quantity") +
                        ", Date: " + rs.getTimestamp("sold_date");
                purchaseHistory.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        historylist.setItems(purchaseHistory);
    }
}

