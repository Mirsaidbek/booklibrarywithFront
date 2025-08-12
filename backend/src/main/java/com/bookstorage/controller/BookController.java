package com.bookstorage.controller;

import com.bookstorage.dto.BookDto;
import com.bookstorage.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/books")
@Tag(name = "Book Management", description = "Book management APIs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Get user's books", description = "Get all books for the current user with search and pagination")
    public ResponseEntity<Page<BookDto>> getUserBooks(Authentication authentication,
                                                     @RequestParam(required = false) String search,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                     @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            String username = authentication.getName();
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<BookDto> books = bookService.getUserBooks(username, search, pageable);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Get a specific book by its ID")
    public ResponseEntity<BookDto> getBookById(Authentication authentication, @PathVariable Long id) {
        try {
            String username = authentication.getName();
            BookDto book = bookService.getBookById(id, username);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book with optional cover image and book file")
    public ResponseEntity<BookDto> createBook(Authentication authentication,
                                            @RequestParam String title,
                                            @RequestParam(required = false) String author,
                                            @RequestParam(required = false) String description,
                                            @RequestParam(required = false) MultipartFile coverImage,
                                            @RequestParam(required = false) MultipartFile bookFile) {
        try {
            String username = authentication.getName();
            BookDto book = bookService.createBook(username, title, author, description, coverImage, bookFile);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book with new information")
    public ResponseEntity<BookDto> updateBook(Authentication authentication,
                                            @PathVariable Long id,
                                            @RequestParam(required = false) String title,
                                            @RequestParam(required = false) String author,
                                            @RequestParam(required = false) String description,
                                            @RequestParam(required = false) MultipartFile coverImage,
                                            @RequestParam(required = false) MultipartFile bookFile) {
        try {
            String username = authentication.getName();
            BookDto book = bookService.updateBook(id, username, title, author, description, coverImage, bookFile);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Delete a book and its associated files")
    public ResponseEntity<Void> deleteBook(Authentication authentication, @PathVariable Long id) {
        try {
            String username = authentication.getName();
            bookService.deleteBook(id, username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
