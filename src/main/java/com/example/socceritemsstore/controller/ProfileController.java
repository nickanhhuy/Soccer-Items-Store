package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.service.S3Service;
import com.example.socceritemsstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private S3Service s3Service;
    
    @Value("${upload.path:uploads/avatars}")
    private String uploadPath;
    
    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }
    
    @PostMapping("/update")
    public String updateProfile(@RequestParam String fullName,
                               @RequestParam String phone,
                               @RequestParam String email,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            userService.updateProfile(username, fullName, phone, email);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("New passwords do not match");
            }
            
            if (newPassword.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters");
            }
            
            String username = authentication.getName();
            userService.changePassword(username, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
    
    @PostMapping("/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Please select a file to upload");
            }
            
            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }
            
            // Validate file size (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("File size must be less than 5MB");
            }
            
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            
            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Generate unique filename
            String fileExtension = "";
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = user.getUser_id() + "_" + System.currentTimeMillis() + fileExtension;
            
            // Save file to upload directory
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            // Update user avatar URL (relative path for web access)
            String avatarUrl = "/uploads/avatars/" + fileName;
            userService.updateAvatar(username, avatarUrl);
            
            redirectAttributes.addFlashAttribute("successMessage", "Avatar uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload avatar: " + e.getMessage());
        }
        return "redirect:/profile";
    }
    
    @PostMapping("/delete-avatar")
    public String deleteAvatar(Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            
            // Delete the physical file if it exists
            if (user.getAvatarUrl() != null && user.getAvatarUrl().startsWith("/uploads/avatars/")) {
                String fileName = user.getAvatarUrl().substring("/uploads/avatars/".length());
                Path filePath = Paths.get(uploadPath).resolve(fileName);
                Files.deleteIfExists(filePath);
            }
            
            // Update user avatar URL to null
            userService.updateAvatar(username, null);
            
            redirectAttributes.addFlashAttribute("successMessage", "Avatar deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete avatar: " + e.getMessage());
        }
        return "redirect:/profile";
    }
    
    @PostMapping("/delete-account")
    public String deleteAccount(Authentication authentication, 
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            
            // Delete the user account
            userService.deleteAccount(username);
            
            // Logout the user
            request.getSession().invalidate();
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Your account has been deleted successfully. We're sorry to see you go!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to delete account: " + e.getMessage());
            return "redirect:/profile";
        }
    }
}
