package com.example.socceritemsstore.repository;

import com.example.socceritemsstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUsername(String username);
    // filter descending order by date
    List<Order> findAllByOrderByOrderDateDesc();
}
