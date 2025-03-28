package com.qpa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qpa.entity.Payment;
import com.qpa.service.PayEmailService;
import com.qpa.service.PaymentService;

import jakarta.mail.MessagingException;


@RestController
@RequestMapping("/PAYMENT")
public class PaymentController {
	
@Autowired
private PaymentService paymentService;

@GetMapping("/ui/booking/payment")
    public String paymentPage() {
        return "payment";
    }

    @Autowired
private PayEmailService emailService;

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
