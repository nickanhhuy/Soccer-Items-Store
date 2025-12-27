package com.example.socceritemsstore.service;

import com.example.socceritemsstore.exception.ResourceNotFoundException;
import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {
    @Autowired
    private ItemRepo itemRepo;

    public void addItemOrder(Item item) {
        validateItem(item);
        itemRepo.save(item);
    }
    
    public Item addItem(Item item) {
        validateItem(item);
        return itemRepo.save(item);
    }
    
    public Item updateItem(Long id, Item item) {
        Item existing = itemRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        
        validateItem(item);
        
        existing.setName(item.getName());
        existing.setPrice(item.getPrice());
        existing.setQuantity(item.getQuantity());
        existing.setCategory(item.getCategory());
        existing.setGender(item.getGender());
        existing.setImage(item.getImage());
        
        return itemRepo.save(existing);
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }
    
    public Item getItem(Long id) {
        return itemRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    public void deleteItem(Long id) {
        if (!itemRepo.existsById(id)) {
            throw new ResourceNotFoundException("Item not found with id: " + id);
        }
        itemRepo.deleteById(id);
    }
    
    public void updateStock(Long itemId, int quantity) {
        Item item = getItem(itemId);
        int newQuantity = item.getQuantity() - quantity;
        
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock for item: " + item.getName());
        }
        
        item.setQuantity(newQuantity);
        itemRepo.save(item);
    }
    
    private void validateItem(Item item) {
        if (item == null) {
            throw new RuntimeException("Item cannot be null");
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new RuntimeException("Item name is required");
        }
        if (item.getPrice() == null || item.getPrice() < 0.01 || item.getPrice() > 9999.99) {
            throw new RuntimeException("Invalid price. Must be between 0.01 and 9999.99");
        }
        if (item.getQuantity() == null || item.getQuantity() < 0 || item.getQuantity() > 9999) {
            throw new RuntimeException("Invalid quantity. Must be between 0 and 9999");
        }
    }
}
