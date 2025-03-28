package com.qpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.String;


@Controller
@RequestMapping("/ui/booking")
public class PaymentUIController {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:7212/PAYMENT";

    public PaymentUIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/payment")
    public String paymentPage(@RequestParam("bookingId") Long bookingId,
    @RequestParam("amount") double amount,
    Model model) {
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("amount", amount);
        return "payment";
    }

    public String processPayment(
            @RequestParam("bookId") Long bookId,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("amount") double amount,
            @RequestParam("cardNumber") String cardNumber,
            @RequestParam("expiry") String expiry,
            @RequestParam("cvv") String cvv,
            RedirectAttributes redirectAttributes) {
        // Payment processing logic here
        redirectAttributes.addFlashAttribute("message", "Payment successful for Booking ID: " + bookId);
        return "redirect:/ui/booking/viewAll";
    }


}
