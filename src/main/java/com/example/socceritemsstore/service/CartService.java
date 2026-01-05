package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.Cart;
import com.example.socceritemsstore.model.CartItem;
import com.example.socceritemsstore.repository.CartItemRepo;
import com.example.socceritemsstore.repository.CartRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    
    @Autowired
    private CartRepo cartRepo;
    
    @Autowired
    private CartItemRepo cartItemRepo;
    
    /**
     * Get or create cart for user
     */
    public Cart getCartByUsername(String username) {
        return cartRepo.findByUsername(username)
                .orElseGet(() -> {
                    Cart newCart = new Cart(username);
                    return cartRepo.save(newCart);
                });
    }
    
    /**
     * Add item to cart
     */
    @Transactional
    public void addToCart(String username, String productName, String category, String size, 
                         int quantity, double price, String gender, String image) {
        Cart cart = getCartByUsername(username);
        
        // Check if item with same product and size already exists
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductName().equals(productName) && item.getSize().equals(size))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity if item exists
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepo.save(item);
        } else {
            // Add new item
            CartItem newItem = new CartItem(productName, category, size, quantity, price, gender, image);
            cart.addItem(newItem);
            cartRepo.save(cart);
        }
    }
    
    /**
     * Update item quantity
     */
    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (quantity <= 0) {
            removeFromCart(cartItemId);
        } else {
            item.setQuantity(quantity);
            cartItemRepo.save(item);
        }
    }
    
    /**
     * Remove item from cart
     */
    @Transactional
    public void removeFromCart(Long cartItemId) {
        cartItemRepo.deleteById(cartItemId);
    }
    
    /**
     * Clear entire cart
     */
    @Transactional
    public void clearCart(String username) {
        cartRepo.findByUsername(username).ifPresent(cart -> {
            cart.getCartItems().clear();
            cartRepo.save(cart);
        });
    }
    
    /**
     * Get cart item count
     */
    public int getCartItemCount(String username) {
        return cartRepo.findByUsername(username)
                .map(Cart::getTotalItems)
                .orElse(0);
    }
    
    /**
     * Get cart total amount
     */
    public double getCartTotal(String username) {
        return cartRepo.findByUsername(username)
                .map(Cart::getTotalAmount)
                .orElse(0.0);
    }
}
