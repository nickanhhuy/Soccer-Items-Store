package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.model.Order;
import com.example.socceritemsstore.model.OrderItem;
import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import com.example.socceritemsstore.service.ItemService;
import com.example.socceritemsstore.service.OrderService;
import com.example.socceritemsstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserRepo userRepo;

    // Menu page
    @GetMapping("/menu")
    public String menu(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            System.out.println("DEBUG: Authenticated user -> " + username);
        }
        // keep existing orders in session intact
        if (session.getAttribute("orders") == null) {
            session.setAttribute("orders", new java.util.ArrayList<Item>());
        }
        model.addAttribute("items", itemService.getAllItems());
        return "menu";
    }

    @PostMapping("/order")
    public String placeOrder(@RequestParam String name,
                             @RequestParam String category,
                             @RequestParam Integer quantity,
                             @RequestParam String gender,
                             @RequestParam Double price,
                             @RequestParam String size,
                             @RequestParam String image,
                             Authentication authentication,
                             HttpServletRequest request,
                             Model model) {
        HttpSession session = request.getSession();
        List<Item> orders = (List<Item>) session.getAttribute("orders");

        //add a new order into the cart
        List<String> sizes = Collections.singletonList(size);
        Item item = new Item(name, category, quantity, price, gender, sizes);
        item.setImage(image);
        orders.add(item);
        session.setAttribute("orders", orders);

        //total
        double total = orders.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        model.addAttribute("items",orders);
        model.addAttribute("total", total);


        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        System.out.println("DEBUG: total = " + total);
        return "order";
    }
    @GetMapping("/order")
    public String showOrderPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "order";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "checkout";
    }
    
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String fullName,
                                   @RequestParam String address,
                                   @RequestParam String city,
                                   @RequestParam String state,
                                   @RequestParam String zipCode,
                                   @RequestParam String phone,
                                   @RequestParam String paymentMethod,
                                   @RequestParam Double total,
                                   Model model,
                                   Authentication authentication,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        String username = "Guest";
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
            model.addAttribute("username", username);
        }
        
        try {
            // Get cart items from session
            List<Item> cartItems = (List<Item>) session.getAttribute("orders");
            if (cartItems == null || cartItems.isEmpty()) {
                model.addAttribute("errorMessage", "Your cart is empty");
                return "order";
            }
            
            // Create and save order to database
            Order newOrder = new Order(username, fullName, address, city, state, zipCode, phone, paymentMethod, total);
            
            // Add cart items to order
            for (Item item : cartItems) {
                OrderItem orderItem = new OrderItem(
                    item.getName(),
                    item.getCategory(),
                    item.getSizes() != null && !item.getSizes().isEmpty() ? item.getSizes().get(0) : "N/A",
                    item.getQuantity(),
                    item.getPrice(),
                    item.getImage()
                );
                newOrder.addOrderItem(orderItem);
            }
            
            // Save order to database
            System.out.println("DEBUG: Attempting to save order for user: " + username);
            Order savedOrder = orderService.saveOrder(newOrder);
            System.out.println("DEBUG: Order saved successfully with ID: " + savedOrder.getId());
            
            // Pass order details to checkout page
            model.addAttribute("fullName", fullName);
            model.addAttribute("address", address);
            model.addAttribute("city", city);
            model.addAttribute("state", state);
            model.addAttribute("zipCode", zipCode);
            model.addAttribute("phone", phone);
            model.addAttribute("paymentMethod", paymentMethod);
            model.addAttribute("total", total);
            model.addAttribute("successMessage", "Order placed successfully!");
            
            // Clear the cart after successful checkout
            session.setAttribute("orders", new ArrayList<Item>());
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to save order - " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error processing order: " + e.getMessage());
            
            // Return to order page with error
            List<Item> cartItems = (List<Item>) session.getAttribute("orders");
            if (cartItems != null) {
                double cartTotal = cartItems.stream()
                        .mapToDouble(i -> i.getPrice() * i.getQuantity())
                        .sum();
                model.addAttribute("items", cartItems);
                model.addAttribute("total", cartTotal);
            }
            return "order";
        }
        
        return "checkout";
    }
    
    @GetMapping("/history")
    public String orderHistory(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            
            // Get orders from database for this user
            List<com.example.socceritemsstore.model.Order> dbOrders = orderService.getOrdersByUsername(username);
            System.out.println("DEBUG: Found " + dbOrders.size() + " orders for user: " + username);
            for (com.example.socceritemsstore.model.Order order : dbOrders) {
                System.out.println("DEBUG: Order ID: " + order.getId() + ", Total: " + order.getTotalAmount() + ", Items: " + order.getOrderItems().size());
            }
            model.addAttribute("dbOrders", dbOrders);
        }
        
        // Also get current session orders (cart)
        List<Item> sessionOrders = (List<Item>) session.getAttribute("orders");
        if (sessionOrders == null) {
            sessionOrders = new ArrayList<>();
        }
        
        // Calculate total for session orders
        double total = sessionOrders.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        
        model.addAttribute("orders", sessionOrders);
        model.addAttribute("total", total);
        
        return "history";
    }
    
    //For admin only
    @GetMapping("/admin")
    public String addItem(Model model, Authentication authentication) {
        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return "error/403";
        }
        
        // Check if user is admin
        String username = authentication.getName();
        Optional<User> userOptional = userRepo.findByUserName(username);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if ("ADMIN".equals(user.getRole())) {
                model.addAttribute("username", username);
                model.addAttribute("items", itemService.getAllItems());
                model.addAttribute("item", new Item());
                
                // Get all orders for admin
                List<com.example.socceritemsstore.model.Order> allOrders = orderService.getAllOrders();
                model.addAttribute("allOrders", allOrders);
                
                return "admin";
            }
        }
        
        // If not admin, show 403 error page
        return "error/403";
    }
    // Admin: Save item with validation
    @PostMapping("/admin/saveItem")
    public String saveItem(@Valid @ModelAttribute("item") Item item, 
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes,
                          Authentication authentication) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Add error messages to model
            model.addAttribute("items", itemService.getAllItems());
            model.addAttribute("allOrders", orderService.getAllOrders());
            
            if (authentication != null && authentication.isAuthenticated()) {
                model.addAttribute("username", authentication.getName());
            }
            
            // Return to admin page with errors
            return "admin";
        }
        
        try {
            itemService.addItemOrder(item);
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving product: " + e.getMessage());
        }
        
        return "redirect:/admin";
    }
    
    // Admin: Delete item
    @GetMapping("/admin/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return "redirect:/admin";
    }
    
    // Admin: Edit item (for now, just redirect back - you can add edit form later)
    @GetMapping("/admin/edit/{id}")
    public String editItem(@PathVariable Long id, Model model) {
        Item item = itemService.getItem(id);
        if (item != null) {
            model.addAttribute("item", item);
            model.addAttribute("items", itemService.getAllItems());
            model.addAttribute("allOrders", orderService.getAllOrders());
            model.addAttribute("editMode", true);
            return "admin";
        }
        return "redirect:/admin";
    }
}
