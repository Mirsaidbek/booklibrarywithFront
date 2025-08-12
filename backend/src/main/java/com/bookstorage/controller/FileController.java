package com.bookstorage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload.path}")
    private String uploadPath;

    @GetMapping("/{subdirectory}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String subdirectory, 
                                            @PathVariable String filename) {
        logger.debug("📁 File request - subdirectory: {}, filename: {}", subdirectory, filename);
        
        try {
            Path filePath = Paths.get(uploadPath + subdirectory + "/" + filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filename);
                logger.debug("📁 File served successfully - {}:{} ({} bytes)", subdirectory, filename, resource.contentLength());
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                logger.warn("📁 File not found - {}:{}", subdirectory, filename);
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            logger.error("📁 Error serving file - {}:{} - {}", subdirectory, filename, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/default-book-cover.jpg")
    public ResponseEntity<Resource> serveDefaultBookCover() {
        try {
            Path filePath = Paths.get(uploadPath + "defaults/default-book-cover.jpg");
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"default-book-cover.jpg\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/default-profile.jpg")
    public ResponseEntity<Resource> serveDefaultProfilePhoto() {
        try {
            Path filePath = Paths.get(uploadPath + "defaults/default-profile.jpg");
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"default-profile.jpg\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "epub" -> "application/epub+zip";
            case "mobi" -> "application/x-mobipocket-ebook";
            default -> "application/octet-stream";
        };
    }
}
