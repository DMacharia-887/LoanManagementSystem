package com.usiu.loan.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import com.usiu.loan.model.Client;
import com.usiu.loan.MainApp;
import com.usiu.loan.database.DatabaseHelper; 

public class RegisterClientController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField idNumberField;
    private MainApp mainApp;
    

    @FXML
    public void handleSubmit(ActionEvent event) {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String idNumber = idNumberField.getText();
        // Validate inputs
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        // Save client to database (using a helper method)
        Client client = new Client(0, fullName, email, phone, idNumber);
        boolean success = DatabaseHelper.saveClient(client);
        
        if (success) {
            showAlert("Success", "Client registered successfully!");
        } else {
            showAlert("Error", "There was an error registering the client.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void handleBackToDashboard() {
        if (mainApp != null) {
            mainApp.showDashboard();
            System.out.println("Back To Dashboard...");
        }
    }
}
