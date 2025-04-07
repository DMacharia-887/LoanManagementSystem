package com.usiu.loan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.usiu.loan.controller.LoginController;
import com.usiu.loan.controller.RegisterClientController;
import com.usiu.loan.controller.DashboardController;
import com.usiu.loan.controller.DeleteClientController;
import com.usiu.loan.controller.LoanApplicationController;
import com.usiu.loan.controller.LoanApprovalController;
import com.usiu.loan.controller.LoanRepaymentController;
import com.usiu.loan.controller.ViewClientsController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage; // Primary stage for the application

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage; // Set the primary stage

        // Load login.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // Get the controller and pass this instance of MainApp
        LoginController controller = loader.getController();
        controller.setMainApp(this); // Set the MainApp reference

        stage.setScene(scene);
        stage.setTitle("Loan Management System - Login");
        stage.show();
    }

    public void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
    
            // Get the controller and pass this instance of MainApp
            DashboardController controller = loader.getController();
            controller.setMainApp(this); // Set the MainApp reference
    
            primaryStage.setScene(scene); // Switch the scene on the primary stage
            primaryStage.setTitle("Dashboard");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showRegistration() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/registration.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            // Get the controller and pass this instance of MainApp
            RegisterClientController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Client Registration");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showViewClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewClients.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
    
            // Get the controller and pass the MainApp instance
            ViewClientsController controller = loader.getController();
            controller.setMainApp(this); // Set the MainApp reference
            controller.setConnection(connect()); // Pass the database connection
    
            primaryStage.setScene(scene);
            primaryStage.setTitle("View Clients");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showLoanApproval() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoanApproval.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
    
            LoanApprovalController controller = loader.getController();
            controller.setMainApp(this);
    
            primaryStage.setScene(scene); // Switch the scene on the primary stage
            primaryStage.setTitle("Loan Approval");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoanApplication() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoanApplication.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            // Get the controller and set the MainApp reference
            LoanApplicationController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Loan Application");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRepayments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoanRepayment.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            // Get the controller and set the MainApp reference
            LoanRepaymentController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Loan Repayments");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDeleteClient() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DeleteClient.fxml"));
        Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // Get the controller and pass this instance of MainApp
        DeleteClientController controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.setScene(scene); // Switch the scene on the primary stage
        primaryStage.setTitle("Delete Client");
        primaryStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        launch(args); // This is a static method, so it can call Application.launch() directly
    }

    public Connection connect() {
        try {
            // Database connection details
            String url = "jdbc:mariadb://127.0.0.1:3306/loan_system"; // Replace with your database URL
            String user = "root"; // Replace with your database username
            String password = ""; // Replace with your database password

            // Establish and return the connection
            return java.sql.DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
            return null; // Return null if the connection fails
        }
    }
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 768); // Set default width and height
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
    
            // Get the controller and pass this instance of MainApp
            LoginController controller = loader.getController();
            controller.setMainApp(this);
    
            primaryStage.setScene(scene); // Switch the scene on the primary stage
            primaryStage.setTitle("Loan Management System - Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}