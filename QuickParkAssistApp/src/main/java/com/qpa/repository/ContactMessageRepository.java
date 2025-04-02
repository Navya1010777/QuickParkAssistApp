package com.qpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qpa.entity.ContactMessage;
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}