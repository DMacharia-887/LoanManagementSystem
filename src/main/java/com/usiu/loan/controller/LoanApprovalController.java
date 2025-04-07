package com.usiu.loan.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;

import com.usiu.loan.MainApp;

public class LoanApprovalController {
    private Connection conn;

    private MainApp mainApp;
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp; // Assign the passed MainApp instance to the field
      }

    @FXML
    private ComboBox<String> loanComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private DatePicker approvalDatePicker;

    @FXML
    private TextArea remarksArea;

    @FXML
    public void initialize() {
        connectToDatabase();
        loadLoans();
        statusComboBox.setItems(FXCollections.observableArrayList("APPROVED", "REJECTED"));
        approvalDatePicker.setValue(LocalDate.now());
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/loan_system", "root", "");
        } catch (SQLException e) {
            showInfoAlert("Database Error", "Failed to connect to database: " + e.getMessage());
        }
    }

    private void loadLoans() {
        String sql = "SELECT l.id, c.full_name, l.amount FROM loans l JOIN clients c ON l.client_id = c.id";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            loanComboBox.getItems().clear();

            while (rs.next()) {
                int loanId = rs.getInt("id");
                String clientName = rs.getString("full_name");
                double loanAmount = rs.getDouble("amount");

                String loanEntry = loanId + " - " + clientName + " (Amount: KES " + loanAmount + ")";
                loanComboBox.getItems().add(loanEntry);
            }
        } catch (SQLException e) {
            showInfoAlert("Error Loading Loans", e.getMessage());
        }
    }

    @FXML
    private void handleApprovalSubmit() {
        String loanEntry = loanComboBox.getSelectionModel().getSelectedItem();
        String status = statusComboBox.getSelectionModel().getSelectedItem();
        LocalDate approvalDate = approvalDatePicker.getValue();
        String remarks = remarksArea.getText();
    
        if (loanEntry == null || status == null || approvalDate == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }
    
        try {
            // Extract the loan ID from the selected entry
            int loanId = Integer.parseInt(loanEntry.split(" - ")[0]);
    
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE loans SET status = ?, approval_date = ?, remarks = ? WHERE id = ?")) {
                stmt.setString(1, status);
                stmt.setDate(2, java.sql.Date.valueOf(approvalDate));
                stmt.setString(3, remarks);
                stmt.setInt(4, loanId);
    
                stmt.executeUpdate();
                showInfoAlert("Success", "Loan status updated successfully.");
                clearForm();
            }
        } catch (NumberFormatException e) {
            showInfoAlert("Error", "Invalid loan ID format.");
        } catch (SQLException e) {
            e.printStackTrace();
            showInfoAlert("Error", "An error occurred while updating loan status.");
        }
    }
    @FXML
    private void handleBackToDashboard() {
        if (mainApp != null) {
            mainApp.showDashboard();
            System.out.println("Back To Dashboard...");
        }
    }

private void showInfoAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/loan_system", "root", "");
    }

    private void clearForm() {
        loanComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
        approvalDatePicker.setValue(LocalDate.now());
        remarksArea.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}