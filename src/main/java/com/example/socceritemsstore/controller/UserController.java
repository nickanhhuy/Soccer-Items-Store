package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    //Register Endpoint
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String userName,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            // Validate username
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Username is required");
            }
            if (userName.length() < 3 || userName.length() > 20) {
                throw new IllegalArgumentException("Username must be between 3 and 20 characters");
            }
            if (!userName.matches("^[a-zA-Z0-9_]+$")) {
                throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores");
            }
            
            // Validate email
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("Please enter a valid email address (e.g., user@example.com)");
            }
            
            // Validate password
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters long");
            }
            if (password.length() > 100) {
                throw new IllegalArgumentException("Password must not exceed 100 characters");
            }
            
            // Register user
            userService.registration(userName, password, email);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            // Validation errors
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userName", userName);
            model.addAttribute("email", email);
            return "register";
        } catch (RuntimeException e) {
            // Business logic errors (username/email exists)
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userName", userName);
            model.addAttribute("email", email);
            return "register";
        } catch (Exception e) {
            // Unexpected errors
            model.addAttribute("errorMessage", "Registration failed. Please try again later.");
            model.addAttribute("userName", userName);
            model.addAttribute("email", email);
            return "register";
        }
    }
    
    //Login endpoint
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    //Logout: the user session will be cleared
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // clear the user is in the session
        return "redirect:/login?logout";
    }
}
