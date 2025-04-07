package com.usiu.loan.controller;

import com.usiu.loan.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private MainApp mainApp;  // Add MainApp reference

    // Method to set the reference to MainApp
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "admin123".equals(password)) {
            errorLabel.setText("Login successful!");
            mainApp.showDashboard();  // Use mainApp to show the dashboard
            System.out.println("Login successful!");
        } else {
            errorLabel.setText("Invalid credentials. Try again.");
        }
    }
    @FXML
private void handleQuit() {
    System.out.println("Application is closing...");
    System.exit(0); // Terminate the program
}
}
