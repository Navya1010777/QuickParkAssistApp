package com.qpa.entity;

import java.time.LocalDateTime;

public class PaymentUI {
    private Long id;
    private String  bookingId;
    private String userEmail;
    private Double TotalAmount;
    private String orderId;
    private String paymentStatus;
    private LocalDateTime paymentTime;

    public PaymentUI() {}

    public PaymentUI(String  bookingId, String userEmail, Double TotalAmount, String orderId, String paymentStatus) {
        this.bookingId = bookingId;
        this.userEmail = userEmail;
        this.TotalAmount = TotalAmount;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.paymentTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    

}
