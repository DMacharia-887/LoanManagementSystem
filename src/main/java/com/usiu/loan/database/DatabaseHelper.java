package com.usiu.loan.database;

import com.usiu.loan.model.Client;
import java.sql.*;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:mariadb://127.0.0.1:3306/loan_system";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static boolean saveClient(Client client) {
        String sql = "INSERT INTO clients (full_name, email, phone, id_number) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getFullName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhoneNumber());
            stmt.setString(4, client.getIdNumber()); // Corrected index and method name
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection getConnection() {
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }
}