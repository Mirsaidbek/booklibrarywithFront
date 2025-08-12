package com.bookstorage.controller;

import com.bookstorage.dto.AuthRequest;
import com.bookstorage.dto.AuthResponse;
import com.bookstorage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with email and password")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        logger.info("Registration request received for user: {}", request.getUsername());
        try {
            AuthResponse response = userService.register(request);
            logger.info("Registration successful for user: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration failed for user: {} - {}", request.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with email and password")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        logger.info("Login request received for user: {}", request.getUsername());
        try {
            AuthResponse response = userService.login(request);
            logger.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for user: {} - {}", request.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
