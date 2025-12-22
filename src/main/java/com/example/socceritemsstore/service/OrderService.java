package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.Order;
import com.example.socceritemsstore.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepo orderRepo;
    
    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }
    
    public List<Order> getOrdersByUsername(String username) {
        return orderRepo.findByUsername(username);
    }
    
    public List<Order> getAllOrders() {
        return orderRepo.findAllByOrderByOrderDateDesc();
    }
    
    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }
}
