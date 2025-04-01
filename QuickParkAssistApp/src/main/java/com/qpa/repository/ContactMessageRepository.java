package com.qpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.qpa.entity.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}