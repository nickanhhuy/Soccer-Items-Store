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
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           @RequestParam String userName,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please correct the errors in the form");
            return "register";
        }
        
        try {
            userService.registration(userName, password, email);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            // Handle duplicate username or email
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error during registration. Please try again.");
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
