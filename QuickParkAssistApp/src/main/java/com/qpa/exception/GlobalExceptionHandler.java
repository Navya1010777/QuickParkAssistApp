package com.qpa.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.qpa.dto.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors for input fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>("Validation failed", HttpStatus.BAD_REQUEST.value(), false, errors));
    }

    /**
     * Handles resource not found exceptions
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO<>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), false));
    }

    /**
     * Handles invalid entity exceptions
     */
    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInvalidEntityException(InvalidEntityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), false));
    }

    /**
     * Handles file upload size exceeded exceptions
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ResponseDTO<>(
                        "File size exceeds the maximum allowed limit. Please upload a smaller file.",
                        HttpStatus.PAYLOAD_TOO_LARGE.value(),
                        false
                ));
    }

    /**
     * Handles data integrity violation exceptions (like duplicate entries)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ResponseDTO<>(
                        "Duplicate entry or constraint violation",
                        HttpStatus.CONFLICT.value(),
                        false
                ));
    }

    /**
     * Handles unauthorized access exceptions
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ResponseDTO<Void>> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDTO<>(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), false));
    }

    /**
     * Handles invalid credentials exceptions
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDTO<>(ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), false));
    }

    /**
     * Handles invalid vehicle type exceptions
     */
    @ExceptionHandler(InvalidVehicleTypeException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInvalidVehicleType(InvalidVehicleTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), false));
    }

    /**
     * Handles any unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleGeneralException(Exception ex) {
        // Log the full exception details (consider using a logging framework)
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseDTO<>(
                        "An unexpected error occurred",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        false
                ));
    }
}