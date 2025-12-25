package com.example.socceritemsstore.service;

import com.example.socceritemsstore.exception.InvalidRequestException;
import com.example.socceritemsstore.exception.ResourceNotFoundException;
import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.repository.ItemRepo;
import com.example.socceritemsstore.util.ValidationUtils;
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
            .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
        
        validateItem(item);
        
        existing.setItem_name(item.getItem_name());
        existing.setPrice(item.getPrice());
        existing.setQuantity(item.getQuantity());
        existing.setCategory(item.getCategory());
        existing.setGender(item.getGender());
        existing.setImageUrl(item.getImageUrl());
        
        return itemRepo.save(existing);
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }
    
    public Item getItem(Long id) {
        return itemRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
    }

    public void deleteItem(Long id) {
        if (!itemRepo.existsById(id)) {
            throw new ResourceNotFoundException("Item", "id", id);
        }
        itemRepo.deleteById(id);
    }
    
    public void updateStock(Long itemId, int quantity) {
        Item item = getItem(itemId);
        int newQuantity = item.getQuantity() - quantity;
        
        if (newQuantity < 0) {
            throw new InvalidRequestException("Insufficient stock for item: " + item.getItem_name());
        }
        
        item.setQuantity(newQuantity);
        itemRepo.save(item);
    }
    
    private void validateItem(Item item) {
        if (item == null) {
            throw new InvalidRequestException("Item cannot be null");
        }
        if (ValidationUtils.isNullOrEmpty(item.getItem_name())) {
            throw new InvalidRequestException("Item name is required");
        }
        if (!ValidationUtils.isValidPrice(item.getPrice())) {
            throw new InvalidRequestException("Invalid price. Must be between 0.01 and 9999.99");
        }
        if (!ValidationUtils.isValidQuantity(item.getQuantity())) {
            throw new InvalidRequestException("Invalid quantity. Must be between 0 and 9999");
        }
    }
}
