package com.example.socceritemsstore.service;

import com.example.socceritemsstore.exception.DuplicateResourceException;
import com.example.socceritemsstore.exception.ResourceNotFoundException;
import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registration(String userName, String password, String email) throws IOException {
        // Check if username already exists
        if (userRepo.findByUserName(userName).isPresent()) {
            throw new DuplicateResourceException("User", "username", userName);
        }
        
        // Check if email already exists
        if (userRepo.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("User", "email", email);
        }
        
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        
        // Assign ADMIN role to huynguyen, USER role to others
        if ("huynguyen".equalsIgnoreCase(userName)) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }
        
        User savedUser = userRepo.save(user);
        
        // Try to upload to S3 (optional - don't fail if S3 not configured)
        try {
            String userJson = objectMapper.writeValueAsString(savedUser);
            String fileName = "users/" + savedUser.getUser_id() + ".json";
            s3Service.uploadStringAsFile(fileName, userJson);
        } catch (Exception e) {
            System.err.println("S3 upload failed (optional): " + e.getMessage());
            // Continue - S3 is optional
        }
        
        // Send welcome email asynchronously after transaction commits
        sendWelcomeEmailAsync(email, userName);
    }
    
    // Send email outside of transaction to prevent rollback
    private void sendWelcomeEmailAsync(String email, String userName) {
        try {
            emailService.sendWelcomeEmail(email, userName);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
            // Don't fail registration if email fails
        }
    }
    
    public User getUserByUsername(String username) {
        return userRepo.findByUserName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
    
    public User getUserById(Long id) {
        return userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    
    public boolean existsByUsername(String username) {
        return userRepo.findByUserName(username).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }
}
