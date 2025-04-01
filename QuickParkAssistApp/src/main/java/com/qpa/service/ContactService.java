package com.qpa.service;

import com.qpa.entity.ContactMessage;
import com.qpa.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    private ContactMessageRepository repository;

    public ContactMessage saveMessage(ContactMessage message) {
        return repository.save(message);
    }
}