package com.qpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.qpa.dto.ResponseDTO;
import com.qpa.entity.UserInfo;
import com.qpa.service.AuthService;
import com.qpa.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller to handle requests related to home, dashboard, and contact pages.
 */
@Controller
public class HomeController {

    @Autowired
    private AuthService authUiService; // Service for authentication-related operations

    @Autowired
    private UserService userService; // Service for fetching user details

    /**
     * Handles requests to the home page.
     * Redirects to the login page if the user is not authenticated.
     */
    @GetMapping("/")
    public String homePage(HttpServletRequest request, Model model) {
        if (!authUiService.isAuthenticated(request)) {
            return "redirect:/auth/login"; // Redirects unauthenticated users to the login page
        }
        
        return "index"; // Loads the home page
    }

    /**
     * Handles requests to the dashboard.
     * Redirects to the login page if the user is not authenticated.
     * Fetches user details and passes them to the view.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        if (!authUiService.isAuthenticated(request)) {
            return "redirect:/auth/login"; // Redirects unauthenticated users
        }

        // Fetch user details
        ResponseDTO<UserInfo> response = userService.getUserDetails(request);

        if (!response.isSuccess()) {
            model.addAttribute("error", response.getMessage()); // Adds error message if fetching fails
        } else {
            UserInfo user = response.getData(); // Retrieves user info
            System.out.println("fullName: " + user.getFullName());
            
            // If user has a profile image, pass it to the model
            if (user.getImageUrl() != null) {
                model.addAttribute("imageUrl", user.getImageUrl());
            }
            System.out.println("imageUrl: " + user.getImageUrl());

            model.addAttribute("fullName", user.getFullName()); // Pass full name to the view
        }
        return "dashboard/dashboard"; // Loads the dashboard view
    }

    /**
     * Handles requests to the contact page.
     * Redirects to the login page if the user is not authenticated.
     */
    @GetMapping("/contact")
    public String contactPage(HttpServletRequest request) {
        if (!authUiService.isAuthenticated(request)) {
            return "redirect:/auth/login"; // Redirects unauthenticated users
        }
        return "contact"; // Loads the contact page
    }
}
