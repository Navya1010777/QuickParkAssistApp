package com.qpa.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpotBookingDTO {
    private String razorpayPaymentId;
    private double bookingTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private long slotId;
    private String registrationNumber;

    // ✅ No-Args Constructor
    public SpotBookingDTO() {
    }

    // ✅ All-Args Constructor
    public SpotBookingDTO(String razorpayPaymentId, double bookingTime, LocalDate startDate, LocalDate endDate,
            LocalTime startTime, LocalTime endTime, long slotId, String registrationNumber) {
        this.razorpayPaymentId = razorpayPaymentId;
        this.bookingTime = bookingTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotId = slotId;
        this.registrationNumber = registrationNumber;
    }

    // ✅ Getters and Setters
    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public double getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(double bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }


}
