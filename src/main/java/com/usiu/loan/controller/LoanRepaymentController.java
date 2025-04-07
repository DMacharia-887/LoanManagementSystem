package com.usiu.loan.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.usiu.loan.MainApp;

public class LoanRepaymentController {

    private MainApp mainApp;
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp; // Assign the passed MainApp instance to the field
      }
    @FXML
    private ComboBox<String> loanComboBox;

    @FXML
    private TextField repaymentAmountField;

    @FXML
    private DatePicker repaymentDatePicker;

    private Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/loan_system", "root", "");
        System.out.println("Database connection established: " + (conn != null)); // Debug statement
        return conn;
    }

    @FXML
private void initialize() {
    try (Connection conn = connect();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT l.id, c.full_name, l.amount, l.total_paid " +
             "FROM loans l " +
             "JOIN clients c ON l.client_id = c.id " +
             "WHERE l.status = 'APPROVED'");
         ResultSet rs = stmt.executeQuery()) {

        loanComboBox.getItems().clear();

        while (rs.next()) {
            int loanId = rs.getInt("id");
            String clientName = rs.getString("full_name");
            double loanAmount = rs.getDouble("amount");
            double totalPaid = rs.getDouble("total_paid");

            // Format the loan entry to include loan ID, client name, loan amount, and total paid
            String loanEntry = loanId + " - " + clientName + " (Amount: KES " + loanAmount +
                               ", Total Paid: KES " + totalPaid + ")";
            System.out.println("Loaded loan: " + loanEntry); // Debug statement
            loanComboBox.getItems().add(loanEntry);
        }

        System.out.println("Total loans loaded: " + loanComboBox.getItems().size());
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

@FXML
private void handleRepaymentSubmit(ActionEvent event) {
    String loanEntry = loanComboBox.getValue();
    String amountStr = repaymentAmountField.getText();
    LocalDate date = repaymentDatePicker.getValue();

    if (loanEntry == null || amountStr.isEmpty() || date == null) {
        showAlert("Please fill in all fields.");
        return;
    }

    try {
        // Extract the loan ID from the selected entry
        int loanId = Integer.parseInt(loanEntry.split(" - ")[0]);
        double repaymentAmount = Double.parseDouble(amountStr);

        try (Connection conn = connect()) {
            // Record the repayment
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO repayments (loan_id, amount, repayment_date) VALUES (?, ?, ?)")) {
                stmt.setInt(1, loanId);
                stmt.setBigDecimal(2, new java.math.BigDecimal(repaymentAmount));
                stmt.setDate(3, java.sql.Date.valueOf(date));
                stmt.executeUpdate();
            }

            // Update the total amount paid in the loans table
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE loans SET total_paid = total_paid + ? WHERE id = ?")) {
                stmt.setDouble(1, repaymentAmount);
                stmt.setInt(2, loanId);
                stmt.executeUpdate();
            }

            showAlert("Repayment recorded successfully.");
        }
    } catch (NumberFormatException e) {
        showAlert("Invalid loan ID or repayment amount format.");
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("An error occurred while recording repayment.");
    }
}

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void handleBackToDashboard() {
        if (mainApp != null) {
            mainApp.showDashboard();
            System.out.println("Back To Dashboard...");
        }
    }
} 
