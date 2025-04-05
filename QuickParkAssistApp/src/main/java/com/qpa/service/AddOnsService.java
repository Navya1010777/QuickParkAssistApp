package com.qpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qpa.entity.AddOns;
import com.qpa.entity.VehicleType;
import com.qpa.repository.AddOnRepository;

@Service
public class AddOnsService {
	@Autowired
	private final AddOnRepository addOnRepository;

	public AddOnsService(AddOnRepository addOnRepository) {
		this.addOnRepository = addOnRepository;
	}

	public List<AddOns> getAllAddOns() {
		return addOnRepository.findAll();
	}

	public AddOns getAddOnsById(Long id) {
		return addOnRepository.findById(id).orElseThrow(() -> new RuntimeException("AddOn not found"));
	}

	public List<AddOns> getAddOnsByVehicleType(VehicleType vehicleType) {
		return addOnRepository.findByVehicleType(vehicleType);
	}

	public AddOns saveAddOns(AddOns addOns) {
		return addOnRepository.save(addOns);

	}

}
