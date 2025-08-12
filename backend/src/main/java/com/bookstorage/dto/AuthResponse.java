package com.bookstorage.dto;

import com.bookstorage.entity.UserRole;
import com.bookstorage.entity.UserStatus;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String fullName;
    private String profilePhoto;
    private UserRole role;
    private UserStatus status;

    // Constructors
    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String username, String fullName, 
                       String profilePhoto, UserRole role, UserStatus status) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.profilePhoto = profilePhoto;
        this.role = role;
        this.status = status;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
