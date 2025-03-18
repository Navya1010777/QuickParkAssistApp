package com.bookpayment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.bookpayment.entity.Payment;
import com.bookpayment.service.EmailService;
import com.bookpayment.service.PaymentService;

import jakarta.mail.MessagingException;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment")
    public String paymentPage() {
        return "payment";
    }

    @Autowired
private EmailService emailService;

@PostMapping("/processPayment")
public String processPayment(@RequestParam String bookId, 
                             @RequestParam String userEmail, 
                             @RequestParam Double amount, 
                             Model model) {
    Payment payment = paymentService.processPayment(bookId, userEmail, amount);
    
    try {
            emailService.sendReceipt(userEmail, payment.getOrderId(), bookId, amount);
        } catch (MessagingException e) {
            model.addAttribute("emailError", "Payment successful, but email failed: " + e.getMessage());
        }

    model.addAttribute("orderId", payment.getOrderId());
    model.addAttribute("bookId", bookId);
    model.addAttribute("amount", amount);
    model.addAttribute("userEmail", userEmail);

    return "success";
}
    
}
