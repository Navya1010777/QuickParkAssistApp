package com.qpa.service;

import com.qpa.entity.Payment;
import com.qpa.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(String bookingId, String userEmail, Double totalAmount) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment(bookingId, userEmail, totalAmount, orderId, "SUCCESS");
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return paymentRepository.findPaymentsBetweenDates(startDateTime, endDateTime);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
