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

    public PurchaseHistoryController() {
        dbConnection = new MySQLDatabaseConnection();
        dbConnection.connect(); // Ensure connection is established in constructor
    }

    @FXML
    private void initialize() {
        loadPurchaseHistory();
    }

    private void loadPurchaseHistory() {
        ObservableList<String> purchaseHistory = FXCollections.observableArrayList();
        String sql = "SELECT * FROM soldbooks";

        try (var preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
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
