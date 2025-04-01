package com.qpa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.qpa.entity.ContactMessage;
import com.qpa.service.ContactEmailService;
import com.qpa.service.ContactService;

import jakarta.mail.MessagingException;

@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactEmailService contactEmailService;

    @PostMapping("/api/contact")
    public ResponseEntity<String> saveContactMessage(@RequestBody ContactMessage message) {
        try {
            // Save the message and get the saved entity with generated ID
            ContactMessage savedMessage = contactService.saveMessage(message);
            // Send confirmation email with the generated ID
            contactEmailService.sendConfirmationEmail(savedMessage.getName(), savedMessage.getEmail(), savedMessage.getId());
            return ResponseEntity.ok("Message saved and email sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to process request: " + e.getMessage());
        }
    }
}