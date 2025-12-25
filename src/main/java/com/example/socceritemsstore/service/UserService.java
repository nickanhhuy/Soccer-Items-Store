package com.example.socceritemsstore.service;

import com.example.socceritemsstore.exception.DuplicateResourceException;
import com.example.socceritemsstore.exception.InvalidRequestException;
import com.example.socceritemsstore.exception.ResourceNotFoundException;
import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import com.example.socceritemsstore.util.ValidationUtils;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registration(String userName, String password, String email) throws IOException {
        // Validate inputs
        if (!ValidationUtils.isValidUsername(userName)) {
            throw new InvalidRequestException("Username must be 3-20 characters and contain only letters, numbers, and underscores");
        }
        if (!ValidationUtils.isValidPassword(password)) {
            throw new InvalidRequestException("Password must be at least 6 characters");
        }
        if (!ValidationUtils.isValidEmail(email)) {
            throw new InvalidRequestException("Invalid email format");
        }
        
        // Check if username already exists
        if (userRepo.findByUserName(userName).isPresent()) {
            throw new DuplicateResourceException("User", "username", userName);
        }
        
        // Check if email already exists
        if (userRepo.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("User", "email", email);
        }
        
        User user = new User();
        user.setUserName(ValidationUtils.sanitizeString(userName));
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(ValidationUtils.sanitizeString(email));
        
        // Assign ADMIN role to huynguyen, USER role to others
        if ("huynguyen".equalsIgnoreCase(userName)) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }
        
        User savedUser = userRepo.save(user);
        String userJson = objectMapper.writeValueAsString(savedUser);

        String fileName = "users/" + savedUser.getUser_id() + ".json";
        s3Service.uploadStringAsFile(fileName, userJson);
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
