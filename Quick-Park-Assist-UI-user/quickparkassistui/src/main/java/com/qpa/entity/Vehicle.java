package com.qpa.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Vehicle {
    private Long vehicleId;

    private String registrationNumber;

    private VehicleType vehicleType;

    private String brand;

    private String model;

    private String registrationDate; // Now stored as String

    private boolean isActive = true;

    private UserInfo userObj;

    // Default constructor
    public Vehicle() {
    }

    // Getters and Setters
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public UserInfo getUserObj() {
        return userObj;
    }

    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", vehicleType=" + (vehicleType != null ? vehicleType.name() : "null") +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", isActive=" + isActive +
                ", userId=" + userObj.getUserId() +
                '}';
    }

}
