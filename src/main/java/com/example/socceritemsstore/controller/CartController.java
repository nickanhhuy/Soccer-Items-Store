package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.model.Cart;
import com.example.socceritemsstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * View cart page
     */
    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        Cart cart = cartService.getCartByUsername(username);
        
        model.addAttribute("cart", cart);
        model.addAttribute("username", username);
        model.addAttribute("cartTotal", cart.getTotalAmount());
        model.addAttribute("cartItemCount", cart.getTotalItems());
        
        return "cart";
    }
    
    /**
     * Add item to cart
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam String name,
                           @RequestParam String category,
                           @RequestParam String size,
                           @RequestParam int quantity,
                           @RequestParam double price,
                           @RequestParam(required = false) String gender,
                           @RequestParam String image,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            String username = authentication.getName();
            cartService.addToCart(username, name, category, size, quantity, price, gender, image);
            redirectAttributes.addFlashAttribute("successMessage", "Item added to cart successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add item to cart: " + e.getMessage());
        }
        
        return "redirect:/menu";
    }
    
    /**
     * Update cart item quantity
     */
    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Long itemId,
                                 @RequestParam int quantity,
                                 RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(itemId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Cart updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update cart: " + e.getMessage());
        }
        
        return "redirect:/cart";
    }
    
    /**
     * Remove item from cart
     */
    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable Long itemId,
                                 RedirectAttributes redirectAttributes) {
        try {
            cartService.removeFromCart(itemId);
            redirectAttributes.addFlashAttribute("successMessage", "Item removed from cart!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to remove item: " + e.getMessage());
        }
        
        return "redirect:/cart";
    }
    
    /**
     * Clear entire cart
     */
    @PostMapping("/clear")
    public String clearCart(Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            String username = authentication.getName();
            cartService.clearCart(username);
            redirectAttributes.addFlashAttribute("successMessage", "Cart cleared successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to clear cart: " + e.getMessage());
        }
        
        return "redirect:/cart";
    }
    
    /**
     * Get cart count (AJAX endpoint)
     */
    @GetMapping("/count")
    @ResponseBody
    public int getCartCount(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }
        
        String username = authentication.getName();
        return cartService.getCartItemCount(username);
    }
}
