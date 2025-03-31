package com.qpa.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpa.entity.Payment;
import com.qpa.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(Long bookId, String userEmail, Double amount) {
        // Generate unique Order ID
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment(bookId, amount, orderId, userEmail, "SUCCESS");
        return paymentRepository.save(payment);
    }

}
