package com.bookstorage.service;

import com.bookstorage.dto.BookDto;
import com.bookstorage.entity.Book;
import com.bookstorage.entity.User;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    public Page<BookDto> getUserBooks(String username, String searchTerm, Pageable pageable) {
        logger.debug("Getting books for user: {} with search term: '{}'", username, searchTerm);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    return new RuntimeException("User not found");
                });

        Page<Book> books;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            books = bookRepository.findByOwnerAndSearchTerm(user, searchTerm.trim(), pageable);
            logger.debug("Found {} books for user {} with search term '{}'", books.getTotalElements(), username, searchTerm);
        } else {
            books = bookRepository.findByOwner(user, pageable);
            logger.debug("Found {} books for user {}", books.getTotalElements(), username);
        }

        return books.map(BookDto::new);
    }

    public BookDto getBookById(Long bookId, String username) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user owns the book or is admin
        if (!book.isOwnedBy(user) && !user.isAdmin()) {
            throw new RuntimeException("Access denied");
        }

        return new BookDto(book);
    }

    public BookDto createBook(String username, String title, String author, String description,
                            MultipartFile coverImage, MultipartFile bookFile) {
        logger.info("Creating book for user: {} with title: '{}'", username, title);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    return new RuntimeException("User not found");
                });

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setOwner(user);

        // Handle cover image upload
        if (coverImage != null && !coverImage.isEmpty()) {
            logger.debug("Uploading cover image for book: {}", title);
            String imageUrl = uploadFile(coverImage, "covers");
            book.setImageUrl(imageUrl);
            logger.debug("Cover image uploaded successfully: {}", imageUrl);
        }

        // Handle book file upload
        if (bookFile != null && !bookFile.isEmpty()) {
            logger.debug("Uploading book file for book: {}", title);
            String contentUrl = uploadFile(bookFile, "books");
            book.setContentUrl(contentUrl);
            logger.debug("Book file uploaded successfully: {}", contentUrl);
        }

        Book savedBook = bookRepository.save(book);
        logger.info("Book created successfully: {} with ID: {}", title, savedBook.getId());
        return new BookDto(savedBook);
    }

    public BookDto updateBook(Long bookId, String username, String title, String author, 
                            String description, MultipartFile coverImage, MultipartFile bookFile) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!book.isOwnedBy(user)) {
            throw new RuntimeException("Access denied");
        }

        if (title != null && !title.trim().isEmpty()) {
            book.setTitle(title);
        }
        if (author != null) {
            book.setAuthor(author);
        }
        if (description != null) {
            book.setDescription(description);
        }

        // Handle cover image upload
        if (coverImage != null && !coverImage.isEmpty()) {
            // Delete old cover image if exists
            if (book.getImageUrl() != null) {
                deleteFile(book.getImageUrl());
            }
            String imageUrl = uploadFile(coverImage, "covers");
            book.setImageUrl(imageUrl);
        }

        // Handle book file upload
        if (bookFile != null && !bookFile.isEmpty()) {
            // Delete old book file if exists
            if (book.getContentUrl() != null) {
                deleteFile(book.getContentUrl());
            }
            String contentUrl = uploadFile(bookFile, "books");
            book.setContentUrl(contentUrl);
        }

        Book savedBook = bookRepository.save(book);
        return new BookDto(savedBook);
    }

    public void deleteBook(Long bookId, String username) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!book.isOwnedBy(user)) {
            throw new RuntimeException("Access denied");
        }

        // Delete associated files
        if (book.getImageUrl() != null) {
            deleteFile(book.getImageUrl());
        }
        if (book.getContentUrl() != null) {
            deleteFile(book.getContentUrl());
        }

        bookRepository.delete(book);
    }

    public Page<BookDto> getAllBooks(String title, String author, Long ownerId, Pageable pageable) {
        Page<Book> books = bookRepository.findByFilters(title, author, ownerId, pageable);
        return books.map(BookDto::new);
    }

    public List<BookDto> getUserBooksByUserId(Long userId) {
        List<Book> books = bookRepository.findByOwnerId(userId);
        return books.stream().map(BookDto::new).collect(Collectors.toList());
    }

    public Page<BookDto> getUserBooksByUserId(Long userId, Pageable pageable) {
        Page<Book> books = bookRepository.findByOwnerIdOrderByCreatedAtDesc(userId, pageable);
        return books.map(BookDto::new);
    }

    private String uploadFile(MultipartFile file, String subdirectory) {
        try {
            String fileName = subdirectory + "_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath + subdirectory + "/");
            
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            return subdirectory + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    private void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadPath + filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            // Log error but don't throw exception
            System.err.println("Failed to delete file: " + filePath);
        }
    }
}
