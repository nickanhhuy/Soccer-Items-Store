package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import com.example.socceritemsstore.service.AnalyticsService;
import com.example.socceritemsstore.service.ItemService;
import com.example.socceritemsstore.service.OrderService;
import com.example.socceritemsstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private UserRepo userRepo;
    
    // Admin Dashboard - redirect to products
    @GetMapping
    public String adminDashboard() {
        return "redirect:/admin/products";
    }
    
    // Product Management
    @GetMapping("/products")
    public String productManagement(Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "error/403";
        }
        
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("item", new Item());
        return "admin/products";
    }
    
    // Current Inventory
    @GetMapping("/inventory")
    public String inventory(Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "error/403";
        }
        
        model.addAttribute("items", itemService.getAllItems());
        return "admin/inventory";
    }
    
    // Customer Orders
    @GetMapping("/orders")
    public String orders(Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "error/403";
        }
        
        model.addAttribute("allOrders", orderService.getAllOrders());
        return "admin/orders";
    }
    
    // User Management
    @GetMapping("/users")
    public String users(Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "error/403";
        }
        
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("editMode", false);
        return "admin/users";
    }
    
    // Analytics Dashboard
    @GetMapping("/analytics")
    public String analytics(Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "error/403";
        }
        
        model.addAttribute("stats", analyticsService.getDashboardStats());
        model.addAttribute("recentOrders", analyticsService.getRecentOrders(10));
        model.addAttribute("topCustomers", analyticsService.getTopCustomers(5));
        model.addAttribute("revenueByMonth", analyticsService.getRevenueByMonth());
        return "admin/analytics";
    }
    
    // Save Item
    @PostMapping("/saveItem")
    public String saveItem(@Valid @ModelAttribute("item") Item item, 
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix the form errors");
            return "redirect:/admin/products";
        }
        
        try {
            itemService.addItemOrder(item);
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving product: " + e.getMessage());
        }
        
        return "redirect:/admin/products";
    }
    
    // Delete Item - POST method for form submission
    @PostMapping("/delete/{id}")
    public String deleteItemPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItem(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/admin/inventory";
    }
    
    // Delete Item - GET method for backward compatibility
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteItemPost(id, redirectAttributes);
    }
    
    // Edit Item
    @GetMapping("/edit/{id}")
    public String editItem(@PathVariable Long id, Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "error/403";
        }
        
        Item item = itemService.getItem(id);
        if (item != null) {
            model.addAttribute("item", item);
            model.addAttribute("editMode", true);
            return "admin/products";
        }
        return "redirect:/admin/products";
    }
    
    // Create User
    @PostMapping("/createUser")
    public String createUser(@RequestParam String userName,
                            @RequestParam String password,
                            @RequestParam String email,
                            @RequestParam(required = false) String fullName,
                            @RequestParam(required = false) String phone,
                            @RequestParam String role,
                            RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(userName, password, email, fullName, phone, role);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    // Update User
    @PostMapping("/updateUser")
    public String updateUser(@RequestParam Long userId,
                            @RequestParam String fullName,
                            @RequestParam String email,
                            @RequestParam(required = false) String phone,
                            @RequestParam String role,
                            RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userId, fullName, email, phone, role);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    // Edit User - GET method to show edit form
    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, Model model, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return "error/403";
        }
        
        try {
            User user = userService.getUserById(id);
            model.addAttribute("editUser", user);
            model.addAttribute("allUsers", userService.getAllUsers());
            model.addAttribute("newUser", new User());
            model.addAttribute("editMode", true);
            return "admin/users";
        } catch (Exception e) {
            return "redirect:/admin/users";
        }
    }
    
    // Delete User - POST method for form submission
    @PostMapping("/deleteUser/{id}")
    public String deleteUserPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    // Delete User - GET method for backward compatibility
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteUserPost(id, redirectAttributes);
    }
    
    // Toggle User Role
    @GetMapping("/toggleRole/{id}")
    public String toggleUserRole(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            String newRole = "ADMIN".equals(user.getRole()) ? "USER" : "ADMIN";
            userService.updateUserRole(id, newRole);
            redirectAttributes.addFlashAttribute("successMessage", "User role updated to " + newRole);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating role: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    // Helper method to check if user is admin
    private boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String email = authentication.getName();
        Optional<User> userOptional = userRepo.findByEmail(email);
        
        return userOptional.isPresent() && "ADMIN".equals(userOptional.get().getRole());
    }
}