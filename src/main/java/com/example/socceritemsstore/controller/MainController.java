package com.example.socceritemsstore.controller;

//import com.example.socceritemsstore.service.OrderService;
import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.service.ItemService;
import com.example.socceritemsstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @Autowired
    private ItemService itemService;
    // Menu page
    @GetMapping("/menu")
    public String menu(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            System.out.println("DEBUG: Authenticated user -> " + username);
        }
        model.addAttribute("items", itemService.getAllItems());
        return "menu";
    }
    //For admin only
    @GetMapping("/admin")
    public String addItem(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        model.addAttribute("item", new Item());
        return "admin";
    }
    // Admin: Save item
    @PostMapping("/admin/saveItem")
    public String saveItem(@ModelAttribute Item item) {
        itemService.addItemOrder(item);
        return "redirect:/menu";
    }
}
