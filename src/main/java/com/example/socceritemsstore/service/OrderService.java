//package com.example.socceritemsstore.service;
//
//import com.example.socceritemsstore.model.Item;
//import com.example.socceritemsstore.repository.OrderRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class OrderService {
//    @Autowired
//    private OrderRepo orderRepo;
//
//    public void addOrder(Item item_order) {
//        orderRepo.save(item_order);
//    }
//
//    public List<Item> getAllOrders() {
//        return orderRepo.findAll();
//    }
//    public List<Item> getOrdersByCustomerName(String customer_name) {
//        return orderRepo.findByCustomerName(customer_name);
//    }
//}
