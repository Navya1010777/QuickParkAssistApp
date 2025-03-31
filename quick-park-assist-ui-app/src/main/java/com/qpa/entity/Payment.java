package com.qpa.entity;

import java.time.LocalDateTime;

public class Payment {

    private Long id;
    private Long bookingId;
    private Double TotalAmount;
    private String orderId;
    private String paymentStatus;
    private LocalDateTime paymentTime;

    public Payment() {
    }

    public Payment(Long bookingId, Double TotalAmount, String orderId, String paymentStatus,
            LocalDateTime paymentTime) {
        this.bookingId = bookingId;
        this.TotalAmount = TotalAmount;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.paymentTime = paymentTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
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
