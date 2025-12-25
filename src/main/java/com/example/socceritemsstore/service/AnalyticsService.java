package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.Order;
import com.example.socceritemsstore.repository.ItemRepo;
import com.example.socceritemsstore.repository.OrderRepo;
import com.example.socceritemsstore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    
    @Autowired
    private OrderRepo orderRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private ItemRepo itemRepo;
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total counts
        stats.put("totalOrders", orderRepo.count());
        stats.put("totalUsers", userRepo.count());
        stats.put("totalItems", itemRepo.count());
        
        // Revenue calculations
        List<Order> allOrders = orderRepo.findAll();
        double totalRevenue = allOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        stats.put("totalRevenue", totalRevenue);
        
        // Today's stats
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<Order> todayOrders = allOrders.stream()
                .filter(order -> order.getOrderDate().isAfter(startOfDay))
                .collect(Collectors.toList());
        
        stats.put("todayOrders", todayOrders.size());
        double todayRevenue = todayOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        stats.put("todayRevenue", todayRevenue);
        
        // This month's stats
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<Order> monthOrders = allOrders.stream()
                .filter(order -> order.getOrderDate().isAfter(startOfMonth))
                .collect(Collectors.toList());
        
        stats.put("monthOrders", monthOrders.size());
        double monthRevenue = monthOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        stats.put("monthRevenue", monthRevenue);
        
        // Average order value
        double avgOrderValue = allOrders.isEmpty() ? 0 : totalRevenue / allOrders.size();
        stats.put("avgOrderValue", avgOrderValue);
        
        return stats;
    }
    
    public List<Order> getRecentOrders(int limit) {
        List<Order> allOrders = orderRepo.findAllByOrderByOrderDateDesc();
        return allOrders.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    public Map<String, Integer> getTopCustomers(int limit) {
        List<Order> allOrders = orderRepo.findAll();
        
        Map<String, Integer> customerOrderCount = allOrders.stream()
                .collect(Collectors.groupingBy(
                        Order::getUsername,
                        Collectors.summingInt(e -> 1)
                ));
        
        return customerOrderCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    
    public Map<String, Double> getRevenueByMonth() {
        List<Order> allOrders = orderRepo.findAll();
        
        Map<String, Double> revenueByMonth = new LinkedHashMap<>();
        
        allOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().getYear() + "-" + 
                                String.format("%02d", order.getOrderDate().getMonthValue()),
                        Collectors.summingDouble(Order::getTotalAmount)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> revenueByMonth.put(entry.getKey(), entry.getValue()));
        
        return revenueByMonth;
    }
}
