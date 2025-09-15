package com.example.socceritemsstore.repository;

import com.example.socceritemsstore.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<Item, Long> {
}
