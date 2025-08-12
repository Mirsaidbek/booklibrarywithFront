package com.bookstorage.dto;

import com.bookstorage.entity.Book;
import com.bookstorage.entity.User;

import java.time.LocalDateTime;

public class BookDto {

    private Long id;
    private String title;
    private String author;
    private String description;
    private String imageUrl;
    private String contentUrl;
    private Long ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public BookDto() {}

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.description = book.getDescription();
        this.imageUrl = book.getImageUrl();
        this.contentUrl = book.getContentUrl();
        this.createdAt = book.getCreatedAt();
        this.updatedAt = book.getUpdatedAt();
        
        if (book.getOwner() != null) {
            this.ownerId = book.getOwner().getId();
            this.ownerName = book.getOwner().getFullName();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
    public String getDefaultImageUrl() {
        return this.imageUrl != null ? this.imageUrl : "/api/files/default-book-cover.jpg";
    }

    public boolean isOwnedBy(Long userId) {
        return this.ownerId != null && this.ownerId.equals(userId);
    }
}
