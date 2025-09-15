package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepo itemRepo;

    public void addItemOrder(Item item) {
        itemRepo.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }
    public Item getItem(Long id) {
        return itemRepo.findById(id).orElse(null);
    }

    public void deleteItem(Long id) {
        itemRepo.deleteById(id);
    }
}
