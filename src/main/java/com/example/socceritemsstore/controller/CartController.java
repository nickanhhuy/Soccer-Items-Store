package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.model.Cart;
import com.example.socceritemsstore.model.CartItem;
import com.example.socceritemsstore.model.Order;
import com.example.socceritemsstore.model.OrderItem;
import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import com.example.socceritemsstore.service.CartService;
import com.example.socceritemsstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserRepo userRepo;
    
    /**
     * View cart page
     */
    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String email = authentication.getName();
        Optional<User> userOptional = userRepo.findByEmail(email);
        
        if (!userOptional.isPresent()) {
            return "redirect:/login";
        }
        
        User user = userOptional.get();
        String username = user.getUserName();
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
            String email = authentication.getName();
            Optional<User> userOptional = userRepo.findByEmail(email);
            
            if (!userOptional.isPresent()) {
                return "redirect:/login";
            }
            
            String username = userOptional.get().getUserName();
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
            String email = authentication.getName();
            Optional<User> userOptional = userRepo.findByEmail(email);
            
            if (!userOptional.isPresent()) {
                return "redirect:/login";
            }
            
            String username = userOptional.get().getUserName();
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
        
        String email = authentication.getName();
        Optional<User> userOptional = userRepo.findByEmail(email);
        
        if (!userOptional.isPresent()) {
            return 0;
        }
        
        String username = userOptional.get().getUserName();
        return cartService.getCartItemCount(username);
    }
    
    /**
     * Proceed to checkout from cart
     */
    @GetMapping("/checkout")
    public String proceedToCheckout(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        String email = authentication.getName();
        Optional<User> userOptional = userRepo.findByEmail(email);
        
        if (!userOptional.isPresent()) {
            return "redirect:/login";
        }
        
        User user = userOptional.get();
        String username = user.getUserName();
        Cart cart = cartService.getCartByUsername(username);
        
        if (cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }
        
        // Calculate shipping
        double shipping = cart.getTotalAmount() >= 99 ? 0 : 9.99;
        double total = cart.getTotalAmount() + shipping;
        
        model.addAttribute("username", username);
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cart.getTotalAmount());
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);
        
        return "order";
    }
    
    /**
     * Process checkout and create order
     */
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String fullName,
                                   @RequestParam String address,
                                   @RequestParam String city,
                                   @RequestParam String state,
                                   @RequestParam String zipCode,
                                   @RequestParam String phone,
                                   @RequestParam String paymentMethod,
                                   Model model,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            String email = authentication.getName();
            Optional<User> userOptional = userRepo.findByEmail(email);
            
            if (!userOptional.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return "redirect:/cart";
            }
            
            User user = userOptional.get();
            String username = user.getUserName();
            
            // Get cart items
            Cart cart = cartService.getCartByUsername(username);
            if (cart.getCartItems().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty");
                return "redirect:/cart";
            }
            
            // Calculate total with shipping
            double cartTotal = cart.getTotalAmount();
            double shipping = cartTotal >= 99 ? 0 : 9.99;
            double total = cartTotal + shipping;
            
            // Create order
            Order newOrder = new Order(username, fullName, address, city, state, zipCode, phone, paymentMethod, total);
            
            // Add cart items to order
            for (CartItem cartItem : cart.getCartItems()) {
                OrderItem orderItem = new OrderItem(
                    cartItem.getProductName(),
                    cartItem.getCategory(),
                    cartItem.getSize(),
                    cartItem.getQuantity(),
                    cartItem.getPrice(),
                    cartItem.getImage()
                );
                newOrder.addOrderItem(orderItem);
            }
            
            // Save order
            Order savedOrder = orderService.saveOrder(newOrder);
            System.out.println("DEBUG: Order saved successfully with ID: " + savedOrder.getId());
            
            // Clear cart after successful order
            cartService.clearCart(username);
            
            // Pass order details to success page
            model.addAttribute("username", username);
            model.addAttribute("fullName", fullName);
            model.addAttribute("address", address);
            model.addAttribute("city", city);
            model.addAttribute("state", state);
            model.addAttribute("zipCode", zipCode);
            model.addAttribute("phone", phone);
            model.addAttribute("paymentMethod", paymentMethod);
            model.addAttribute("total", total);
            
            return "checkout";
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to process checkout - " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing order: " + e.getMessage());
            return "redirect:/cart/checkout";
        }
    }
}
