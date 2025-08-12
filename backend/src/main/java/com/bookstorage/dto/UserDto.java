package com.bookstorage.dto;

import com.bookstorage.entity.User;
import com.bookstorage.entity.UserRole;
import com.bookstorage.entity.UserStatus;

import java.time.LocalDateTime;

public class UserDto {

    private Long id;
    private String fullName;
    private String username;
    private String profilePhoto;
    private UserRole role;
    private UserStatus status;
    private int booksCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public UserDto() {}

    public UserDto(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.profilePhoto = user.getProfilePhoto();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.booksCount = user.getBooksCount();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(int booksCount) {
        this.booksCount = booksCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isAdmin() {
        return UserRole.ADMIN.equals(this.role);
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    public String getDefaultProfilePhoto() {
        return this.profilePhoto != null ? this.profilePhoto : "/api/files/default-profile.jpg";
    }
}
