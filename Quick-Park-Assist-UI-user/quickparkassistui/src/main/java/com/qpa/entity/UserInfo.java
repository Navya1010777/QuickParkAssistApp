package com.qpa.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserInfo {
    private Long userId;

    private LocalDate dateOfRegister = LocalDate.now();

    private String fullName;

    private String email;

    private String dob;  // Optional

    private String contactNumber; // Optional

    private UserType userType; // Optional

    private String address; // Optional

    private Status status = Status.ACTIVE; // Default status, but can be null in DB

    private String imageUrl; // Optional, can be null

    // Default constructor
    public UserInfo() {
    }

    protected void onCreate() {
        if (dateOfRegister == null) {
            dateOfRegister = LocalDate.now();
        }
        if (status == null) {
            status = Status.ACTIVE;
        }
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(LocalDate dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFormattedDateOfRegister() {
        return dateOfRegister != null ? dateOfRegister.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }


    public enum Status { ACTIVE, INACTIVE }
}
