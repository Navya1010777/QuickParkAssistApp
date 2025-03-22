package com.qpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
	
	@Id
	private Long userId;

	
	/*
	 userId
	dateOfReg
	firstName
	lastName
	emailId
	contactNumber
	userType - can be VehicleOwner/SlotWoner
 	address
	status - Active / Inactive

	If needed include any other attributes
	
	You can have the userName and password here
	 */
}
