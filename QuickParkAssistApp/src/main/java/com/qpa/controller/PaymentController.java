package com.qpa.controller;

import com.qpa.entity.Payment;
import com.qpa.service.PayEmailService;
import com.qpa.service.PaymentService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PayEmailService emailService;

    @PostMapping("/process")
    public Payment processPayment(@RequestBody Payment payment) {
        Payment processedPayment = paymentService.processPayment(payment.getBookingId(), payment.getUserEmail(), payment.getTotalAmount());
        try {
            emailService.sendReceipt(processedPayment.getUserEmail(), processedPayment.getOrderId(), processedPayment.getBookingId(), processedPayment.getTotalAmount());
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
}
