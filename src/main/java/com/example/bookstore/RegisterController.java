package com.example.bookstore;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class RegisterController {

    @FXML
    private Button registerbtn;

    @FXML
    private TextField username_txt;

    @FXML
    private TextField pass_txt;

    private MySQLDatabaseConnection dbConnection;

    public RegisterController() {
        dbConnection = new MySQLDatabaseConnection();
    }

    @FXML
    void register(ActionEvent event) {
        String username = username_txt.getText();
        String password = pass_txt.getText();

        dbConnection.connect();

        try {
            // Check if the username already exists
            if (checkUsernameExists(username)) {
                showAlert(Alert.AlertType.ERROR, "Username Exists", "Username already exists. Please choose another one.");
                return;
            }

            // If the username doesn't exist, proceed with registration
            String query = "INSERT INTO users (username, password, usertype) VALUES (?, ?, ?)";
            PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, "customer"); // Set the user type to 'customer'
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register user. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        } finally {
            dbConnection.disconnect();
        }
    }

    private boolean checkUsernameExists(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next(); // If resultSet.next() returns true, the username exists
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void loginnav(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the register page.");
        }
    }
}
