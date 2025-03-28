package com.qpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qpa.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
}