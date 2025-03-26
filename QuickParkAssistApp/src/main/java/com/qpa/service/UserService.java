package com.qpa.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qpa.dto.RegisterDTO;
import com.qpa.entity.AuthUser;
import com.qpa.entity.UserInfo;
import com.qpa.entity.UserType;
import com.qpa.entity.Vehicle;
import com.qpa.exception.InvalidEntityException;
import com.qpa.exception.UnauthorizedAccessException;
import com.qpa.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VehicleService vehicleService;
    private final AuthService authService;
    private final CloudinaryService cloudinaryService;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, VehicleService vehicleService,
            AuthService authService, CloudinaryService cloudinaryService,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.vehicleService = vehicleService;
        this.authService = authService;
        this.cloudinaryService = cloudinaryService;
        this.emailService = emailService;
    }

    public UserInfo getCurrentUserProfile(HttpServletRequest request) throws InvalidEntityException {
        Long userId = authService.getUserId(request);
        return getUserById(userId);
    }

    public void uploadUserAvatar(MultipartFile file, HttpServletRequest request)
            throws IOException, InvalidEntityException {
        String imageUrl = cloudinaryService.uploadImage(file);
        UserInfo user = getUserById(authService.getUserId(request));

        // Delete existing image if any
        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            cloudinaryService.deleteImage(user.getImageUrl());
        }

        user.setImageUrl(imageUrl);
        updateUser(user);
    }

    public void deleteUserAvatar(String imageUrl, HttpServletRequest request)
            throws IOException, InvalidEntityException {
        cloudinaryService.deleteImage(imageUrl);
        UserInfo user = getUserById(authService.getUserId(request));
        user.setImageUrl("");
        updateUser(user);
    }

    public void registerUser(RegisterDTO request, HttpServletResponse response, HttpServletRequest httpRequest)
            throws Exception {
        // Check if the user is already logged in
        if (authService.isAuthenticated(httpRequest)) {
            throw new IllegalStateException("User is already logged in");
        }

        // Check if email is already registered
        if (existsByEmail(request.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        // Prevent registration as ADMIN
        if (request.getUserType() == UserType.ADMIN) {
            throw new IllegalArgumentException("ADMIN Role is forbidden");
        }

        // Create new user
        UserInfo user = new UserInfo();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setUserType(request.getUserType());
        user.setUsername(request.getUsername());
        user = addUser(user);

        // Create authentication details
        AuthUser authUser = new AuthUser();
        authUser.setPassword(request.getPassword());
        authUser.setEmail(request.getEmail());
        authUser.setUser(user);

        // Add authentication
        if (!authService.addAuth(authUser, response)) {
            throw new RuntimeException("Authentication failed");
        }

        // Send registration email
        emailService.sendSimpleMail(request.getEmail(), request.getFullName(),
                "Registration Successful as " + request.getUserType());
    }

    public void updateUserDetails(UserInfo user, HttpServletRequest request) throws InvalidEntityException {
        if (!authService.isAuthenticated(request)) {
            throw new UnauthorizedAccessException("Unauthorized request");
        }

        try {
            updateUser(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new DataIntegrityViolationException("Duplicate entry for email");
            }
            throw e;
        }
    }

    public UserInfo addUser(UserInfo user) {
        return userRepository.save(user);
    }

    public UserInfo getUserById(Long id) throws InvalidEntityException {
        return userRepository.findById(id)
                .orElseThrow(() -> new InvalidEntityException("User not found"));
    }

    public List<UserInfo> getAllUsers() {
        return userRepository.findAll();
    }

    public UserInfo updateUser(UserInfo user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id, HttpServletResponse response) throws InvalidEntityException {
        UserInfo user = getUserById(id);
        try {
            authService.deleteAuth(id, response);
            userRepository.delete(user);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidEntityException("Cannot delete user. Related entities exist.");
        }
    }

    public UserInfo viewUserByVehicleId(Long vehicleId) throws InvalidEntityException {
        return vehicleService.getVehicleById(vehicleId).getUserObj();
    }

    public UserInfo findByBookingId(Long bookingId) throws InvalidEntityException {
        Vehicle vehicle = vehicleService.findByBookingId(bookingId);
        return viewUserByVehicleId(vehicle.getVehicleId());
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserType checkUserType(Long userId) throws InvalidEntityException {
        UserInfo user = getUserById(userId);
        return user.getUserType();
    }
}