package com.example.bookstore;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private Button loginbtn;

    @FXML
    private TextField usertxt;

    @FXML
    private TextField passtxt;

    @FXML
    private Button registerbtn;

    private MySQLDatabaseConnection dbConnection;

    public LoginController() {
        dbConnection = new MySQLDatabaseConnection();
    }

    @FXML
    void login(ActionEvent event) {
        String username = usertxt.getText();
        String password = passtxt.getText();

        dbConnection.connect();

        try {
            String query = "SELECT user_id, usertype FROM users WHERE username=? AND password=?";
            PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String userType = resultSet.getString("usertype");
                System.out.println("Login successful. User ID: " + userId); // Debugging statement
                openPage(userType, userId);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Credentials", "Please enter valid username and password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        } finally {
            dbConnection.disconnect();
        }
    }


    @FXML
    void register(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registerpage.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the register page.");
        }
    }


    private void openPage(String userType, int userId) {
        Stage stage = (Stage) loginbtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root;
            if ("admin".equals(userType)) {
                loader.setLocation(getClass().getResource("mainmenu.fxml"));
                root = loader.load();
            } else if ("customer".equals(userType)) {
                loader.setLocation(getClass().getResource("customer_view.fxml"));
                root = loader.load();

                BookController customerController = loader.getController();
                customerController.setCurrentUserId(userId); // Pass user ID
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid User Type", "Invalid user type.");
                return;
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the page.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
