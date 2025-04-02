package com.qpa.controller;

import com.qpa.entity.Payment;
import com.qpa.service.PayEmailService;
import com.qpa.service.PaymentService;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qpa.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PayEmailService emailService;

    @PostMapping("/process")
    public Payment processPayment(@RequestBody Payment payment) {
        Payment processedPayment = paymentService.processPayment(payment.getBookingId(), payment.getUserEmail(),
                payment.getTotalAmount());
        try {
            emailService.sendReceipt(processedPayment.getUserEmail(), processedPayment.getOrderId(),
                    processedPayment.getBookingId(), processedPayment.getTotalAmount());
        } catch (MessagingException e) {
            System.out.println("Email failed: " + e.getMessage());
        }
        return processedPayment;
    }

    @GetMapping("/history")
    public List<Payment> getPaymentHistory(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        if (start != null && end != null) {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            return paymentService.getPaymentsBetweenDates(startDate, endDate);
        }
        return paymentService.getAllPayments();
    }

    @GetMapping("/getAllAdminPayments")
    public List<Payment> getAdminPayments(HttpServletRequest request) {
        Long userId = authService.getUserId(request);
        return paymentService.getAllPaymentsByAdmin(userId);
    }

    @GetMapping("/viewByBookingId/{bookingId}")
    public ResponseEntity<Payment> getMethodName(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

}
