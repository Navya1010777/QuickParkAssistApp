package com.qpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qpa.entity.Payment;
import com.qpa.service.PayEmailService;
import com.qpa.service.PaymentService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/PAYMENT")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PayEmailService emailService;

    @PostMapping("/processPayment")
    public ResponseEntity<Boolean> processPayment(@RequestParam String bookId,
            @RequestParam String userEmail,
            @RequestParam Double amount,
            Model model) {

        try {
            paymentService.processPayment(bookId, userEmail, amount);
            // emailService.sendReceipt(userEmail, payment.getOrderId(), bookId, amount);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);

        }
    }

}
