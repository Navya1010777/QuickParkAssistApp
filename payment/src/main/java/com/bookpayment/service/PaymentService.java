package com.bookpayment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bookpayment.entity.Payment;
import com.bookpayment.repository.PaymentRepository;

import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(String bookId, String userEmail, Double amount) {
        // Generate unique Order ID
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment(bookId, userEmail, amount, orderId, "SUCCESS");
        return paymentRepository.save(payment);
    }
    
}
