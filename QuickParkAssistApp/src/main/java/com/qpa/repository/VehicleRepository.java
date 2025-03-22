package com.qpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qpa.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    public Vehicle findByRegistrationNumber(String registrationNumber);
}
