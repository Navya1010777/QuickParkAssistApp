package com.qpa.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qpa.service.CustomRestTemplateService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ui/booking")
public class PaymentUIController {

    private final CustomRestTemplateService restTemplate;
    private final String BASE_URL = "http://localhost:7212/PAYMENT";

    public PaymentUIController(CustomRestTemplateService restTemplate) {
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

    @PostMapping("/paymentProcess")
    public String processPayment(
            @RequestParam("bookId") Long bookId,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("amount") double amount,
            @RequestParam("cardNumber") String cardNumber,
            @RequestParam("expiry") String expiry,
            @RequestParam("cvv") String cvv,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        // Payment processing logic here
        Boolean isSuccessResponse = restTemplate
                .post("/PAYMENT/processPayment?bookId=" + bookId + "&userEmail=" + userEmail + "&amount=" + amount,
                        null, request, new ParameterizedTypeReference<Boolean>() {
                        })
                .getBody();
        boolean isSuccess = isSuccessResponse != null && isSuccessResponse;

        if (!isSuccess) {
            redirectAttributes.addFlashAttribute("message", "Payment UnSuccessful for Booking ID: " + bookId);
        } else {
            redirectAttributes.addFlashAttribute("message", "Payment successful for Booking ID: " + bookId);
        }
        return "redirect:/ui/booking/viewAll";
    }

}
