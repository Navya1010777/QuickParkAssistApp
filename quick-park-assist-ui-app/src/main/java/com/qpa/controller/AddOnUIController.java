package com.qpa.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qpa.entity.AddOns;
import com.qpa.entity.VehicleType;
import com.qpa.service.CustomRestTemplateService;

import jakarta.servlet.http.HttpServletRequest;

@Controller

@RequestMapping("/addons")
public class AddOnUIController {
    @Autowired
    private CustomRestTemplateService restTemplate;

    private static final String BASE_URL = "/addons";

    @GetMapping("/addAddonForm")
    public String addAddonForm(Model model) {
        model.addAttribute("addon", new AddOns());
        model.addAttribute("vehicleTypes", Arrays.asList(VehicleType.values())); // Add enum values for dropdown
        return "addons/addAddon";
    }

    @PostMapping("/addAddon")
    public String addAddon(@ModelAttribute AddOns addon, Model model, HttpServletRequest request) {
        try {
            ResponseEntity<AddOns> response = restTemplate.post(BASE_URL + "/add", addon, request,
                    new ParameterizedTypeReference<AddOns>() {
                    });

            model.addAttribute("message", "Addon added successfully: " + response.getBody().getName());
            return "addons/home";
        } catch (Exception e) {
            return "addons/home";
        }
    }

    @GetMapping("/")
    public String home() {
        return "addons/home";
    }

    @GetMapping("/viewAllAddons")
    public String viewAllAddons(Model model, HttpServletRequest request) {
        ResponseEntity<List<AddOns>> response = restTemplate.get(
                BASE_URL + "/viewAll", request, new ParameterizedTypeReference<List<AddOns>>() {
                });
        List<AddOns> allAddOns = response.getBody();
        for (AddOns addOns: allAddOns){
            System.out.println("addon id: "+addOns.getAddOnId());
        }
        model.addAttribute("addons", response.getBody());
        return "addons/viewAllAddons"; // Renders viewAllAddons.html
    }

    @GetMapping("/viewAddOnByIdForm")
    public String viewAddOnByIdForm() {
        return "addons/viewAddOnByIdForm"; // Renders the form
    }

    @GetMapping("/viewAddOnById")
    public String viewAddOnById(@RequestParam("id") Long id, Model model, HttpServletRequest request) {
        try {
            ResponseEntity<AddOns> response = restTemplate.get(BASE_URL + "/" + id, request,
                    new ParameterizedTypeReference<AddOns>() {
                    });
            model.addAttribute("addon", response.getBody());
            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "AddOn not found with ID: " + id);
        }
        return "addons/viewAddOnById"; // Returns the Thymeleaf template
    }

    @GetMapping("/viewAddOnsByVehicleTypeForm")
    public String viewAddOnsByVehicleTypeForm() {
        return "addons/viewAddOnsByVehicleTypeForm"; // Renders the form page
    }

    @GetMapping("/viewAddOnsByVehicleType")
    public String viewAddOnsByVehicleType(@RequestParam("vehicleType") String vehicleType, Model model,
            HttpServletRequest request) {
        try {
            ResponseEntity<AddOns[]> response = restTemplate.get(BASE_URL + "/vehicle-type/" + vehicleType, request,
                    new ParameterizedTypeReference<AddOns[]>() {
                    });
            model.addAttribute("addons", Arrays.asList(response.getBody()));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "No AddOns found for Vehicle Type: " + vehicleType);
        }
        return "addons/viewAddOnsByVehicleType"; // Returns the Thymeleaf template
    }

}
