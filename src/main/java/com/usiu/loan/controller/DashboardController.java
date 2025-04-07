package com.usiu.loan.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.usiu.loan.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class DashboardController {

    @FXML
    private Button btnRegisterClient;

    @FXML
    private Button btnApplyLoan;

    @FXML
    private Button btnRepayments;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnLogout;

    private MainApp mainApp; // Reference to the MainApp instance

    // Inject MainApp into the controller (should be set from the MainApp class)
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void openLoanApproval() {
        if (mainApp != null) {
            mainApp.showLoanApproval(); // Use MainApp to switch to the Loan Approval page
        }
    }

    @FXML
    private void handleViewClients() {
        if (mainApp != null) {
            mainApp.showViewClients(); // Call the method in MainApp to open the window
        }
    }

    @FXML
    private void handleGenerateReport() {
        String pdfPath = "LoanReport.pdf"; // Path to save the PDF file

        try (Connection conn = mainApp.connect(); // Assuming MainApp has a connect() method
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT c.full_name AS client_name, " +
                 "c.phone, " +
                 "l.amount AS loan_amount, " +
                 "l.status AS loan_status, " +
                 "COALESCE(SUM(r.amount), 0) AS total_paid " +
                 "FROM clients c " +
                 "JOIN loans l ON c.id = l.client_id " +
                 "LEFT JOIN repayments r ON l.id = r.loan_id " +
                 "GROUP BY c.id, l.id");
             ResultSet rs = stmt.executeQuery()) {

            // Create a PDF document
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add a title to the PDF
            document.add(new Paragraph("Loan Report")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            // Add a table to the PDF
            Table table = new Table(5);
            table.addHeaderCell("Client Name");
            table.addHeaderCell("Phone Number");
            table.addHeaderCell("Loan Amount");
            table.addHeaderCell("Loan Status");
            table.addHeaderCell("Total Paid");

            // Populate the table with data from the ResultSet
            while (rs.next()) {
                table.addCell(rs.getString("client_name"));
                table.addCell(rs.getString("phone"));
                table.addCell(String.valueOf(rs.getDouble("loan_amount")));
                table.addCell(rs.getString("loan_status"));
                table.addCell(String.valueOf(rs.getDouble("total_paid")));
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            System.out.println("Report generated successfully: " + pdfPath);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while generating the report.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An unexpected error occurred.");
        }
    }

    @FXML
    public void initialize() {
        // Setting up button actions for all buttons
        btnRegisterClient.setOnAction(e -> handleRegisterClient());
        btnApplyLoan.setOnAction(e -> handleApplyLoan());
        btnRepayments.setOnAction(e -> handleRepaymentSubmit());
        btnReports.setOnAction(e -> handleGenerateReport());
        btnLogout.setOnAction(e -> handleLogout());{
            System.out.println("Logging out...");
            // Optionally go back to login screen
        }
    }

    @FXML
    public void handleRegisterClient() {
        if (mainApp != null) {
            mainApp.showRegistration(); // Show registration screen when the button is clicked
        } else {
            System.out.println("MainApp is not initialized properly.");
        }
    }

    @FXML
    private void handleApplyLoan() {
        if (mainApp != null) {
            mainApp.showLoanApplication(); // Use MainApp to switch to the Loan Application page
        } else {
            System.out.println("MainApp is not initialized properly.");
        }
    }

    @FXML
    private void handleRepaymentSubmit() {
        if (mainApp != null) {
            mainApp.showRepayments(); // Use MainApp to switch to the Loan Repayments page
        } else {
            System.out.println("MainApp is not initialized properly.");
        }
    }
    @FXML
private void handleLogout() {
    if (mainApp != null) {
        System.out.println("Logging out...");
        mainApp.showLogin(); // Navigate back to the login page
    } else {
        System.out.println("mainApp is null. Cannot navigate to the login page.");
    }
}
@FXML
private void openDeleteClient() {
    if (mainApp != null) {
        mainApp.showDeleteClient(); // Navigate to the Delete Client page
    }
}
}