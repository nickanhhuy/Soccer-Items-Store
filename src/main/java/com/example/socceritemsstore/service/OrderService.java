package com.example.socceritemsstore.service;

import com.example.socceritemsstore.exception.InvalidRequestException;
import com.example.socceritemsstore.exception.ResourceNotFoundException;
import com.example.socceritemsstore.model.Order;
import com.example.socceritemsstore.repository.OrderRepo;
import com.example.socceritemsstore.util.ValidationUtils;
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
    
    public Order saveOrder(Order order) {
        validateOrder(order);
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        if (order.getStatus() == null) {
            order.setStatus("Pending");
        }
        return orderRepo.save(order);
    }
    
    public List<Order> getOrdersByUsername(String username) {
        if (ValidationUtils.isNullOrEmpty(username)) {
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
    
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepo.save(order);
    }
    
    private void validateOrder(Order order) {
        if (order == null) {
            throw new InvalidRequestException("Order cannot be null");
        }
        if (ValidationUtils.isNullOrEmpty(order.getUsername())) {
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
