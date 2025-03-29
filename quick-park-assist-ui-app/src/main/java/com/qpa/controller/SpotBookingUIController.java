package com.qpa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qpa.entity.SpotBookingInfo;
import com.qpa.service.VehicleService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.String;
import java.time.LocalDate;

import java.time.LocalTime;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/ui/booking")
public class SpotBookingUIController {

    private final VehicleService vehicleService;

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:7212/api/bookSlot"; // Backend base URL

    public SpotBookingUIController(RestTemplate restTemplate, VehicleService vehicleService) {
        this.restTemplate = restTemplate;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/home")
    public String homePage() {
        return "bookings/home";
    }

    @GetMapping("/add")
    public String showAddBookingPage(Model model, HttpServletRequest request) {
        model.addAttribute("vehicles", vehicleService.findUserVehicle(request).getData());
        return "bookings/addBooking";
    }

    @PostMapping("/save")
    public String saveBooking(
            @ModelAttribute SpotBookingInfo booking,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime,
            @RequestParam long slotId,
            @RequestParam String registrationNumber,
            Model model) {

        try {
            // ✅ Set start and end times
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);

            // ✅ Call Backend API
            ResponseEntity<SpotBookingInfo> response = restTemplate.postForEntity(
                    BASE_URL + "/add/" + slotId + "/" + registrationNumber,
                    booking,
                    SpotBookingInfo.class);
            SpotBookingInfo bookingResponse = response.getBody();
            return "redirect:/ui/booking/payment?bookingId=" + bookingResponse.getBookingId() + "&amount="
                    + bookingResponse.getTotalAmount();

            // Redirect to view all bookings on success

        } catch (HttpClientErrorException e) {
            // ✅ Extract Clean Error Message
            String errorMessage = extractCleanErrorMessage(e.getResponseBodyAsString());

            // ✅ Handle Specific Backend Errors with Detailed Messages
            if (errorMessage.contains("Spot is not available")) {
                model.addAttribute("error",
                        "❌ Spot with ID " + slotId + " is not available. Please select another spot.");
            } else if (errorMessage.contains("Spot is already booked")) {
                model.addAttribute("error", "❌ Spot with ID " + slotId
                        + " is already booked for the given time. Please choose a different time slot.");
            } else if (errorMessage.contains("Start date, start time, and end time must be provided")) {
                model.addAttribute("error",
                        "❌ Start Date, Start Time, and End Time are required fields. Please fill them correctly.");
            } else if (errorMessage.contains("Start date cannot be in the past")) {
                model.addAttribute("error", "❌ Start Date cannot be in the past. Please select a future date.");
            } else if (errorMessage.contains("Vehicle with Registration Number")) {
                model.addAttribute("error", "❌ No vehicle found with Registration Number: " + registrationNumber
                        + ". Please check and try again.");
            } else if (errorMessage.contains("Spot with ID")) {
                model.addAttribute("error",
                        "❌ Spot with ID " + slotId + " does not exist. Please enter a valid Spot ID.");
            } else {
                model.addAttribute("error", "❌ " + errorMessage); // Show the exact backend error
            }

        } catch (Exception e) {
            // ✅ Handle any unexpected errors (without generic messages)
            model.addAttribute("error", "❌ Unexpected error: " + e.getMessage());
        }

        return "bookings/addBooking"; // Return to form with error message if booking fails
    }

    /**
     * ✅ Helper Method: Extracts Clean Error Messages from Backend Responses
     */
    private String extractCleanErrorMessage(String errorResponse) {
        return errorResponse.replaceAll("[{}\"]", "").replaceAll("message:", "").trim();
    }

    /**
     * ✅ Helper Method: Extracts Clean Error Messages from Backend Responses
     */

    @GetMapping("/viewAll")
    public String viewAllBookings(@RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) Long spotId,
            @RequestParam(required = false) String contactNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        List<SpotBookingInfo> bookings = new ArrayList<>();
        String errorMessage = null;

        try {
            if (bookingId != null) {
                ResponseEntity<SpotBookingInfo> response = restTemplate.getForEntity(
                        BASE_URL + "/viewBookingById/" + bookingId, SpotBookingInfo.class);
                if (response.getBody() != null) {
                    bookings = Collections.singletonList(response.getBody());
                }
            } else if (spotId != null) {
                ResponseEntity<List<SpotBookingInfo>> response = restTemplate.exchange(
                        BASE_URL + "/viewBookingBySlotId/" + spotId, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<SpotBookingInfo>>() {
                        });
                bookings = response.getBody() != null ? response.getBody() : new ArrayList<>();
            } else if (contactNumber != null && !contactNumber.trim().isEmpty()) {
                ResponseEntity<List<SpotBookingInfo>> response = restTemplate.exchange(
                        BASE_URL + "/viewByContactNumber/" + contactNumber, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<SpotBookingInfo>>() {
                        });
                bookings = response.getBody() != null ? response.getBody() : new ArrayList<>();
            } else if (startDate != null && endDate != null) {
                ResponseEntity<List<SpotBookingInfo>> response = restTemplate.exchange(
                        BASE_URL + "/viewBetweenDates/" + startDate + "/" + endDate, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<SpotBookingInfo>>() {
                        });
                bookings = response.getBody() != null ? response.getBody() : new ArrayList<>();
            } else {
                ResponseEntity<List<SpotBookingInfo>> response = restTemplate.exchange(
                        BASE_URL + "/viewAllBookings", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<SpotBookingInfo>>() {
                        });
                bookings = response.getBody() != null ? response.getBody() : new ArrayList<>();
            }

            if (bookings.isEmpty()) {
                errorMessage = "No data found as per the filter.";
            }
            model.addAttribute("bookings", bookings);
            model.addAttribute("error", errorMessage);

        } catch (HttpClientErrorException e) {
            try {
                String responseBody = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String backendMessage = jsonNode.has("message") ? jsonNode.get("message").asText()
                        : "No bookings found.";

                // Format error message based on input criteria
                if (bookingId != null) {
                    errorMessage = "No bookings found for Booking ID: " + bookingId;
                } else if (spotId != null) {
                    errorMessage = "No bookings found for Slot ID: " + spotId;
                } else if (contactNumber != null && !contactNumber.trim().isEmpty()) {
                    errorMessage = "No bookings found for Contact Number: " + contactNumber;
                } else if (startDate != null && endDate != null) {
                    errorMessage = "No bookings found between " + startDate + " and " + endDate;
                } else {
                    errorMessage = backendMessage;
                }
            } catch (Exception ex) {
                errorMessage = "No bookings found.";
            }
            model.addAttribute("error", errorMessage);
            model.addAttribute("bookings", Collections.emptyList());
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred while fetching bookings.");
            model.addAttribute("bookings", Collections.emptyList());
        }

        return "bookings/viewAllBooking";
    }

    @GetMapping("/fetchBooking")
    public String fetchBooking(@RequestParam Long bookingId, Model model) {
        try {
            // Fetch booking details from backend
            ResponseEntity<SpotBookingInfo> response = restTemplate.getForEntity(
                    BASE_URL + "/viewBookingById/" + bookingId,
                    SpotBookingInfo.class);
            SpotBookingInfo booking = response.getBody();

            if (booking != null) {
                model.addAttribute("booking", booking);
            } else {
                model.addAttribute("error", "Booking ID with " + bookingId + " does not exist.");
            }
        } catch (HttpClientErrorException e) {
            // Extract meaningful error message
            String errorMessage = extractErrorMessage(e.getResponseBodyAsString(), bookingId);
            model.addAttribute("error", errorMessage);
        } catch (Exception e) {
            model.addAttribute("error", "Booking ID with " + bookingId + " does not exist.");
        }
        return "cancelBooking";
    }

    // Method to extract clean error message
    private String extractErrorMessage(String response, Long bookingId) {
        if (response.contains("not found") || response.contains("does not exist")) {
            return "Booking ID with " + bookingId + " does not exist.";
        } else {
            return "Failed to cancel booking.";
        }
    }

    @GetMapping("/cancel")
    public String showCancelBookingPage() {
        return "cancelBooking";
    }

    @PostMapping("/cancelByBookingId")
    public String cancelBookingById(@RequestParam Long bookingId, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.delete(BASE_URL + "/cancel/" + bookingId);
            redirectAttributes.addFlashAttribute("message", "Booking ID " + bookingId + " cancelled successfully!");
        } catch (HttpClientErrorException e) {
            // Extract only meaningful error message
            String errorMessage = extractErrorMessage(e.getResponseBodyAsString(), bookingId);
            redirectAttributes.addFlashAttribute("error", errorMessage);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Booking ID with " + bookingId + " does not exist.");
        }
        return "redirect:/ui/booking/cancel";
    }

    @GetMapping("/update")
    public String fetchBookingForUpdate(@RequestParam(required = false) Long bookingId, Model model) {
        if (bookingId == null) {
            model.addAttribute("error", "⚠ Please enter a Booking ID!");
            return "updateBooking";
        }

        try {
            // Fetch booking details from backend
            ResponseEntity<SpotBookingInfo> response = restTemplate.getForEntity(
                    BASE_URL + "/viewBookingById/" + bookingId,
                    SpotBookingInfo.class);
            SpotBookingInfo booking = response.getBody();

            if (booking != null) {
                model.addAttribute("booking", booking);
            } else {
                model.addAttribute("error", "⚠ No booking found with the provided ID.");
            }
        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", "⚠ Booking not found! Please check the ID.");
        } catch (Exception e) {
            model.addAttribute("error", "❌ Unable to fetch booking. Please try again.");
        }
        return "updateBooking";
    }

    /**
     * Update Booking Details (End Date and End Time)
     */
    @PostMapping("/update")
    public String updateBooking(
            @RequestParam Long bookingId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate newEndDate,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime newEndTime,
            RedirectAttributes redirectAttributes) {

        try {
            if (bookingId == null) {
                redirectAttributes.addFlashAttribute("error", "⚠ Booking ID is required!");
                return "redirect:/ui/booking/update";
            }

            // Fetch existing booking details
            ResponseEntity<SpotBookingInfo> response = restTemplate.getForEntity(
                    BASE_URL + "/viewBookingById/" + bookingId,
                    SpotBookingInfo.class);
            SpotBookingInfo booking = response.getBody();

            if (booking != null) {
                LocalDate startDate = booking.getStartDate();
                LocalTime startTime = booking.getStartTime();

                // 🛑 Ensure End Date & Time is after Start Date & Time
                if (newEndDate.isBefore(startDate) ||
                        (newEndDate.isEqual(startDate) && newEndTime.isBefore(startTime))) {
                    redirectAttributes.addFlashAttribute("error", "⚠ End date & time must be after start date & time.");
                    return "redirect:/ui/booking/update?bookingId=" + bookingId;
                }

                // Prepare update request
                booking.setEndDate(newEndDate);
                booking.setEndTime(newEndTime);

                // 🛑 First, check if the update conflicts with another booking **before
                // applying the update**
                try {
                    ResponseEntity<String> conflictCheckResponse = restTemplate.postForEntity(
                            BASE_URL + "/check-update-conflict/" + bookingId, // Backend API
                            booking,
                            String.class);

                    // ✅ If no conflict, proceed with update
                    if (conflictCheckResponse.getStatusCode() == HttpStatus.OK) {
                        restTemplate.put(BASE_URL + "/update/" + bookingId, booking);
                        redirectAttributes.addFlashAttribute("message", "✅ Booking updated successfully!");
                    }
                } catch (HttpClientErrorException e) {
                    // ❌ If a 409 Conflict error occurs, STOP the update
                    if (e.getStatusCode() == HttpStatus.CONFLICT) {
                        String errorMessage = extractMessage(e.getResponseBodyAsString());
                        redirectAttributes.addFlashAttribute("error", "❌ " + errorMessage);
                        return "redirect:/ui/booking/update?bookingId=" + bookingId; // Stop update
                    } else {
                        redirectAttributes.addFlashAttribute("error",
                                "❌ Unexpected error: " + e.getResponseBodyAsString());
                        return "redirect:/ui/booking/update?bookingId=" + bookingId; // Stop update
                    }
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "⚠ Booking not found!");
                return "redirect:/ui/booking/update?bookingId=" + bookingId;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Failed to update booking. Please try again.");
            e.printStackTrace();
        }
        return "redirect:/ui/booking/update?bookingId=" + bookingId;
    }

    // HELPER METHOD TO EXTRACT MESSAGE FROM JSON RESPONSE
    private String extractMessage(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.has("message") ? jsonNode.get("message").asText() : "An unexpected error occurred.";
        } catch (Exception e) {
            return "Booking conflict! Please select another time.";
        }
    }

    // View Cancelled Bookings
    @GetMapping("/viewCancelledBookingForm")
    public String showViewCancelledBookingForm() {
        return "viewCancelledBookingForm";
    }

    // Handle form submission and redirect to viewMyBooking.html

    @GetMapping("/cancelledBookings")
    public String viewMyCancelledBookings(@RequestParam(required = false) String contactNumber, Model model) {
        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            // Initial page load without data
            return "viewCancelledBookings";
        }

        try {
            // Call backend API to fetch cancelled bookings
            ResponseEntity<List<SpotBookingInfo>> response = restTemplate.exchange(
                    BASE_URL + "/getCancelledBookingByContactNumber/" + contactNumber,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SpotBookingInfo>>() {
                    });

            List<SpotBookingInfo> cancelledBookings = response.getBody();
            model.addAttribute("cancelledBookings", cancelledBookings);
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Failed to fetch cancelled bookings: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }

        return "viewCancelledBookings";
    }

    private boolean isValidContactNumber(String contactNumber) {
        return contactNumber != null && contactNumber.matches("\\d{10}") && !contactNumber.startsWith("0");
    }
}
