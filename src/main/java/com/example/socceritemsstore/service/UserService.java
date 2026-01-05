package com.example.socceritemsstore.service;

import com.example.socceritemsstore.dto.UserBackupDTO;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
            throw new RuntimeException("Username already exists: " + userName);
        }
        
        // Check if email already exists
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists: " + email);
        }
        
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setEmailVerified(false);  // Email not verified yet
        
        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));  // Token valid for 24 hours
        
        // Assign username "huynguyen" as an ADMIN role. Default role when the user registers is USER.
        if ("huynguyen".equalsIgnoreCase(userName)) {
            user.setRole("ADMIN"); // UPDATE admin role to huynguyen.
            user.setEmailVerified(true);  // Admin doesn't need verification
        } else {
            user.setRole("USER");
        }
        
        User savedUser = userRepo.save(user);
        
        // Backup user data to private S3 bucket
        backupUserToS3(savedUser);
        
        // Send verification email asynchronously after transaction commits
        if (!user.isEmailVerified()) {
            sendVerificationEmailAsync(email, userName, token);
        }
    }
    
    // Send verification email outside of transaction to prevent rollback
    private void sendVerificationEmailAsync(String email, String userName, String token) {
        try {
            emailService.sendVerificationEmail(email, userName, token);
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
            // Don't fail registration if email fails
        }
    }
    
    // Verify email with token and return user
    public User verifyEmailAndGetUser(String token) {
        return userRepo.findByVerificationToken(token)
            .map(user -> {
                if (user.getTokenExpiryDate().isAfter(LocalDateTime.now())) {
                    user.setEmailVerified(true);
                    user.setVerificationToken(null);
                    user.setTokenExpiryDate(null);
                    return userRepo.save(user);
                }
                return null;  // Token expired
            })
            .orElse(null);  // Token not found
    }
    
    public User getUserByUsername(String username) {
        return userRepo.findByUserName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    
    public User getUserById(Long id) {
        return userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
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
    
    public void updateProfile(String username, String fullName, String phone, String email) {
        User user = getUserByUsername(username);
        
        // Check if email is being changed and if it's already taken by another user
        if (!user.getEmail().equals(email) && existsByEmail(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }
        
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setEmail(email);
        userRepo.save(user);
    }
    
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = getUserByUsername(username);
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // Update to new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    // Update the user avatar in profile management
    public void updateAvatar(String username, String avatarUrl) {
        User user = getUserByUsername(username);
        user.setAvatarUrl(avatarUrl);
        userRepo.save(user);
    }

    // Delete user account
    public void deleteAccount(String username) {
        User user = getUserByUsername(username);
        userRepo.delete(user);
    }
    
    // Admin: Update user role
    public void updateUserRole(Long userId, String newRole) {
        User user = getUserById(userId);
        if (!newRole.equals("USER") && !newRole.equals("ADMIN")) {
            throw new RuntimeException("Invalid role. Must be USER or ADMIN");
        }
        user.setRole(newRole);
        userRepo.save(user);
    }
    
    // Admin: Delete user by ID
    public void deleteUserById(Long userId) {
        User user = getUserById(userId);
        userRepo.deleteById(userId);
    }
    
    // Admin: Update user information
    public void updateUser(Long userId, String fullName, String email, String phone, String role) {
        User user = getUserById(userId);
        
        // Check if email is being changed and if it's already taken by another user
        if (!user.getEmail().equals(email)) {
            userRepo.findByEmail(email).ifPresent(existingUser -> {
                if (!existingUser.getUser_id().equals(userId)) {
                    throw new RuntimeException("Email already exists: " + email);
                }
            });
        }
        
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        User savedUser = userRepo.save(user);
        
        // Backup updated user data
        backupUserToS3(savedUser);
    }
    
    // Admin: Create new user
    public void createUser(String userName, String password, String email, String fullName, String phone, String role) {
        // Check if username already exists
        if (userRepo.findByUserName(userName).isPresent()) {
            throw new RuntimeException("Username already exists: " + userName);
        }
        
        // Check if email already exists
        if (userRepo.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists: " + email);
        }
        
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setRole(role);
        
        User savedUser = userRepo.save(user);
        
        // Backup new user data
        backupUserToS3(savedUser);
    }
    
    // Backup user data to S3 (WITHOUT PASSWORD)
    private void backupUserToS3(User user) {
        try {
            // Create DTO without password
            UserBackupDTO backupData = new UserBackupDTO(user);
            String userJson = objectMapper.writeValueAsString(backupData);
            String fileName = "users/backup_" + user.getUser_id() + "_" + System.currentTimeMillis() + ".json";
            s3Service.uploadBackup(fileName, userJson);
        } catch (Exception e) {
            System.err.println("S3 backup failed (optional): " + e.getMessage());
            // Continue - S3 backup is optional
        }
    }
}
