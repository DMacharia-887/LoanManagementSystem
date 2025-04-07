package com.usiu.loan.controller;

import com.usiu.loan.MainApp;
import com.usiu.loan.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewClientsController {
    private MainApp mainApp;
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp; // Assign the passed MainApp instance to the field
        System.out.println("MainApp reference set in ViewClientsController.");
    }

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, Integer> colId;

    @FXML
    private TableColumn<Client, String> colFullName;

    @FXML
    private TableColumn<Client, String> colPhoneNumber;

    @FXML
    private TableColumn<Client, String> colEmail;

    @FXML
    private TableColumn<Client, String> colIdNumber;

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
        loadClients();
    }
     
    private void loadClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM clients");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("id_number")
                ));
            }

            // Bind the data to the TableView
            clientsTable.setItems(clients);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while fetching client details.");
        }
    }

    @FXML
    public void initialize() {
        // Initialize the table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colIdNumber.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
    }
    @FXML
private void handleBackToDashboard() {
    if (mainApp != null) {
        System.out.println("Navigating back to the dashboard...");
        mainApp.showDashboard();
    } else {
        System.out.println("mainApp is null. Cannot navigate back to the dashboard.");
    }
}
    
}