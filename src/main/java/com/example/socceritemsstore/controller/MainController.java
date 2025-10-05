package com.example.socceritemsstore.controller;

//import com.example.socceritemsstore.service.OrderService;
import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.service.ItemService;
//import com.example.socceritemsstore.service.S3Service;
import com.example.socceritemsstore.service.UserService;
import jakarta.persistence.criteria.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ItemService itemService;

    // Menu page
    @GetMapping("/menu")
    public String menu(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            System.out.println("DEBUG: Authenticated user -> " + username);
        }
        // keep existing orders in session intact
        if (session.getAttribute("orders") == null) {
            session.setAttribute("orders", new java.util.ArrayList<Item>());
        }
        model.addAttribute("items", itemService.getAllItems());
        return "menu";
    }

    @PostMapping("/order")
    public String placeOrder(@RequestParam String name,
                             @RequestParam String category,
                             @RequestParam Integer quantity,
                             @RequestParam String gender,
                             @RequestParam Double price,
                             @RequestParam String size,
                             @RequestParam String image,
                             Authentication authentication,
                             HttpServletRequest request,
                             Model model) {
        HttpSession session = request.getSession();
        List<Item> orders = (List<Item>) session.getAttribute("orders");

        //add a new order into the cart
        List<String> sizes = Collections.singletonList(size);
        Item item = new Item(name, category, quantity, price, gender, sizes);
        item.setImage(image);
        orders.add(item);
        session.setAttribute("orders", orders);

        //total
        double total = orders.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        model.addAttribute("items",orders);
        model.addAttribute("total", total);


        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        System.out.println("DEBUG: total = " + total);
        return "order";
    }
    @GetMapping("/order")
    public String showOrderPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "order";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }
        return "checkout";
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
