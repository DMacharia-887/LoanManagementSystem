package com.usiu.loan.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import com.usiu.loan.MainApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteClientController {

    @FXML
    private ComboBox<String> clientComboBox;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        loadClients();
    }

    private void loadClients() {
        try (Connection conn = mainApp.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, full_name FROM clients");
             ResultSet rs = stmt.executeQuery()) {

            clientComboBox.getItems().clear();

            while (rs.next()) {
                int clientId = rs.getInt("id");
                String clientName = rs.getString("full_name");
                clientComboBox.getItems().add(clientId + " - " + clientName);
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load clients: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteClient() {
        String selectedClient = clientComboBox.getValue();
        if (selectedClient == null) {
            showAlert("Error", "Please select a client to delete.");
            return;
        }

        int clientId = Integer.parseInt(selectedClient.split(" - ")[0]);

        try (Connection conn = mainApp.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM clients WHERE id = ?")) {

            stmt.setInt(1, clientId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Client deleted successfully.");
                loadClients(); // Refresh the client list
            } else {
                showAlert("Error", "Failed to delete client.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while deleting the client: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToDashboard() {
        if (mainApp != null) {
            mainApp.showDashboard();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}