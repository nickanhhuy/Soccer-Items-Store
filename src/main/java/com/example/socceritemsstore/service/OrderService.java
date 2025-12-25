package com.example.socceritemsstore.service;

import com.example.socceritemsstore.exception.InvalidRequestException;
import com.example.socceritemsstore.exception.ResourceNotFoundException;
import com.example.socceritemsstore.model.Order;
import com.example.socceritemsstore.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepo orderRepo;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserService userService;
    
    public Order saveOrder(Order order) {
        validateOrder(order);
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        
        Order savedOrder = orderRepo.save(order);
        
        // Send order confirmation email - TEMPORARILY DISABLED FOR TESTING
        /*
        try {
            String customerEmail = userService.getUserByUsername(order.getUsername()).getEmail();
            emailService.sendOrderConfirmation(savedOrder, customerEmail);
        } catch (Exception e) {
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
            // Don't fail the order if email fails
        }
        */
        
        return savedOrder;
    }
    
    public List<Order> getOrdersByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidRequestException("Username cannot be empty");
        }
        return orderRepo.findByUsername(username);
    }
    
    public List<Order> getAllOrders() {
        return orderRepo.findAllByOrderByOrderDateDesc();
    }
    
    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }
    
    private void validateOrder(Order order) {
        if (order == null) {
            throw new InvalidRequestException("Order cannot be null");
        }
        if (order.getUsername() == null || order.getUsername().trim().isEmpty()) {
            throw new InvalidRequestException("Username is required");
        }
        if (order.getTotalAmount() == null || order.getTotalAmount() <= 0) {
            throw new InvalidRequestException("Invalid total amount");
        }
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new InvalidRequestException("Order must contain at least one item");
        }
    }
}
