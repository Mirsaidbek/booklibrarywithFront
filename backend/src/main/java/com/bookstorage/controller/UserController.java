package com.bookstorage.controller;

import com.bookstorage.dto.UserDto;
import com.bookstorage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "User profile management APIs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Get the profile of the currently authenticated user")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        try {
            String username = authentication.getName();
            UserDto user = userService.getCurrentUser(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Update the profile of the currently authenticated user")
    public ResponseEntity<UserDto> updateProfile(Authentication authentication,
                                               @RequestParam(required = false) String fullName,
                                               @RequestParam(required = false) String username) {
        try {
            String currentUsername = authentication.getName();
            UserDto user = userService.updateProfile(currentUsername, fullName, username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/me/password")
    @Operation(summary = "Change password", description = "Change the password of the currently authenticated user")
    public ResponseEntity<UserDto> updatePassword(Authentication authentication,
                                                @RequestParam String currentPassword,
                                                @RequestParam String newPassword) {
        try {
            String username = authentication.getName();
            UserDto user = userService.updatePassword(username, currentPassword, newPassword);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/me/photo")
    @Operation(summary = "Upload profile photo", description = "Upload a profile photo for the currently authenticated user")
    public ResponseEntity<UserDto> uploadProfilePhoto(Authentication authentication,
                                                    @RequestParam("file") MultipartFile file) {
        try {
            String username = authentication.getName();
            UserDto user = userService.uploadProfilePhoto(username, file);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
