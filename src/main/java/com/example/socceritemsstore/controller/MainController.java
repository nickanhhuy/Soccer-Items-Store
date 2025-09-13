package com.example.socceritemsstore.controller;

//import com.example.socceritemsstore.service.OrderService;
import com.example.socceritemsstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
//
//    @Autowired
//    private OrderService orderService;

//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ResourceTransactionManager resourceTransactionManager;

    // Menu page
    @GetMapping("/menu")
    public String menu(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            System.out.println("DEBUG: Authenticated user -> " + username);
        }
        return "menu";
    }


}
