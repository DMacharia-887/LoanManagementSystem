package com.usiu.loan.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

import com.usiu.loan.MainApp;

public class LoanApplicationController {

    private MainApp mainApp;
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp; // Assign the passed MainApp instance to the field
      }

    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private TextField amountField;
    @FXML
    private TextField termField;
    @FXML
    private TextField interestField;
    @FXML
    private DatePicker applicationDatePicker;

    private Connection connection;

    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/loan_system", "root", "");
            System.out.println("Database connection established: " + (connection != null)); // Debug statement
            loadClients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClients() {
        ObservableList<String> clients = FXCollections.observableArrayList();
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, full_name FROM clients")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("full_name");
                String clientEntry = id + " - " + name; // Format the client entry
                System.out.println("Loaded client: " + clientEntry); // Debug statement
                clients.add(clientEntry); // Add the formatted entry to the list
            }
            clientComboBox.setItems(clients);
            System.out.println("Total clients loaded: " + clients.size()); // Debug statement
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitLoan() {
        String clientEntry = clientComboBox.getSelectionModel().getSelectedItem();
        if (clientEntry == null) {
            showAlert(Alert.AlertType.ERROR, "Client not selected.");
            return;
        }
    
        int clientId = Integer.parseInt(clientEntry.split(" - ")[0]);
        String amountText = amountField.getText();
        String termText = termField.getText();
        String interestText = interestField.getText();
        LocalDate appDate = applicationDatePicker.getValue();
    
        if (amountText.isEmpty() || termText.isEmpty() || interestText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all loan details.");
            return;
        }
    
        try {
            double amount = Double.parseDouble(amountText);
            int term = Integer.parseInt(termText);
            double interest = Double.parseDouble(interestText);
    
            String sql = "INSERT INTO loans (client_id, amount, term_months, interest_rate, application_date, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, clientId);
                stmt.setDouble(2, amount);
                stmt.setInt(3, term);
                stmt.setDouble(4, interest);
                stmt.setDate(5, (appDate != null) ? Date.valueOf(appDate) : new Date(System.currentTimeMillis()));
                stmt.setString(6, "PENDING"); // Set the default status to PENDING
    
                stmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Loan application submitted successfully.");
                clearFields();
            }
    
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Please enter valid numeric values.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }
    private void clearFields() {
        amountField.clear();
        termField.clear();
        interestField.clear();
        applicationDatePicker.setValue(null);
        clientComboBox.getSelectionModel().clearSelection();
    }


private void showAlert(Alert.AlertType type, String msg) {
    Alert alert = new Alert(type);
    alert.setTitle("Loan Application");
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
}

@FXML
private void handleBackToDashboard() {
    if (mainApp != null) {
        mainApp.showDashboard();
        System.out.println("Back To Dashboard...");
    }
}
}