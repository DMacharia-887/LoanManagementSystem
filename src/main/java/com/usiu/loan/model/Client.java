package com.usiu.loan.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty fullName;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty email;
    private final SimpleStringProperty idNumber;

    public Client(int id, String fullName, String phoneNumber, String email, String idNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.idNumber = new SimpleStringProperty(idNumber);
    }

    public int getId() {
        return id.get();
    }

    public String getFullName() {
        return fullName.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getIdNumber() {
        return idNumber.get();
    }
}