package com.bookstorage.service;

import com.bookstorage.dto.AuthRequest;
import com.bookstorage.dto.AuthResponse;
import com.bookstorage.dto.UserDto;
import com.bookstorage.entity.User;
import com.bookstorage.entity.UserRole;
import com.bookstorage.entity.UserStatus;
import com.bookstorage.repository.UserRepository;
import com.bookstorage.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${admin.default.email}")
    private String defaultAdminEmail;

    @Value("${admin.default.password}")
    private String defaultAdminPassword;

    @Value("${admin.default.fullName}")
    private String defaultAdminFullName;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user by username: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        logger.debug("User loaded successfully: {} with role: {}", username, user.getRole());
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isActive())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities(user.getRole().name())
                .build();
    }

    public AuthResponse register(AuthRequest request) {
        logger.info("User registration attempt for username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Registration failed - username already exists: {}", request.getUsername());
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getUsername());
        
        logger.info("User registered successfully: {} with ID: {}", savedUser.getUsername(), savedUser.getId());

        return new AuthResponse(token, savedUser.getId(), savedUser.getUsername(),
                savedUser.getFullName(), savedUser.getProfilePhoto(),
                savedUser.getRole(), savedUser.getStatus());
    }

    public AuthResponse login(AuthRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            logger.debug("Authentication successful for user: {}", request.getUsername());
        } catch (Exception e) {
            logger.warn("Authentication failed for user: {} - {}", request.getUsername(), e.getMessage());
            throw e;
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.error("User not found after authentication: {}", request.getUsername());
                    return new RuntimeException("User not found");
                });

        if (!user.isActive()) {
            logger.warn("Login blocked - user is banned: {}", request.getUsername());
            throw new RuntimeException("User is banned");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        logger.info("User logged in successfully: {} with role: {}", user.getUsername(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getUsername(),
                user.getFullName(), user.getProfilePhoto(),
                user.getRole(), user.getStatus());
    }

    public UserDto getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDto(user);
    }

    public UserDto updateProfile(String username, String fullName, String newUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName);
        }

        if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(username)) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(newUsername);
        }

        User savedUser = userRepository.save(user);
        return new UserDto(savedUser);
    }

    public UserDto updatePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser);
    }

    public UserDto uploadProfilePhoto(String username, MultipartFile file) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String fileName = "profile_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath + "profiles/");
            
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Delete old profile photo if exists
            if (user.getProfilePhoto() != null) {
                Path oldPhotoPath = Paths.get(uploadPath + user.getProfilePhoto());
                if (Files.exists(oldPhotoPath)) {
                    Files.delete(oldPhotoPath);
                }
            }

            user.setProfilePhoto("profiles/" + fileName);
            User savedUser = userRepository.save(user);
            return new UserDto(savedUser);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile photo", e);
        }
    }

    // Admin methods
    public Page<UserDto> getAllUsers(String fullName, String username, UserRole role, 
                                   UserStatus status, Pageable pageable) {
        Page<User> users = userRepository.findByFilters(fullName, username, role, status, pageable);
        return users.map(UserDto::new);
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDto(user);
    }

    public UserDto createUser(String fullName, String username, String password, UserRole role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        return new UserDto(savedUser);
    }

    public UserDto updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(status);
        User savedUser = userRepository.save(user);
        return new UserDto(savedUser);
    }

    public void initializeDefaultAdmin() {
        if (!userRepository.existsByUsername(defaultAdminEmail)) {
            User admin = new User();
            admin.setFullName(defaultAdminFullName);
            admin.setUsername(defaultAdminEmail);
            admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
        }
    }
}
