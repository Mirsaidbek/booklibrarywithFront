package com.bookstorage.controller;

import com.bookstorage.dto.BookDto;
import com.bookstorage.dto.UserDto;
import com.bookstorage.entity.UserRole;
import com.bookstorage.entity.UserStatus;
import com.bookstorage.service.BookService;
import com.bookstorage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "Admin user management APIs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Get all users with filtering and pagination")
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam(required = false) String fullName,
                                                   @RequestParam(required = false) String username,
                                                   @RequestParam(required = false) UserRole role,
                                                   @RequestParam(required = false) UserStatus status,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<UserDto> users = userService.getAllUsers(fullName, username, role, status, pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", description = "Get a specific user by their ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/users")
    @Operation(summary = "Create a new user", description = "Create a new user with specified role")
    public ResponseEntity<UserDto> createUser(@RequestParam String fullName,
                                            @RequestParam String username,
                                            @RequestParam String password,
                                            @RequestParam(defaultValue = "USER") UserRole role) {
        try {
            UserDto user = userService.createUser(fullName, username, password, role);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/users/{id}/status")
    @Operation(summary = "Update user status", description = "Ban or unban a user")
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable Long id,
                                                  @RequestParam UserStatus status) {
        try {
            UserDto user = userService.updateUserStatus(id, status);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users/{id}/books")
    @Operation(summary = "Get user's books", description = "Get all books for a specific user")
    public ResponseEntity<Page<BookDto>> getUserBooks(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                     @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<BookDto> books = bookService.getUserBooksByUserId(id, pageable);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/books")
    @Operation(summary = "Get all books", description = "Get all books with filtering and pagination")
    public ResponseEntity<Page<BookDto>> getAllBooks(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String author,
                                                   @RequestParam(required = false) Long ownerId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<BookDto> books = bookService.getAllBooks(title, author, ownerId, pageable);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
