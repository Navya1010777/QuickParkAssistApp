package com.qpa.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Create a map to store the error messages
        Map<String, String> errors = new HashMap<>();

        // Extract all the field errors
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            // Only include the field name and error message
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        // Return a simplified error response
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }



    // Handle other specific exceptions if needed
    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFoundException(InvalidEntityException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
