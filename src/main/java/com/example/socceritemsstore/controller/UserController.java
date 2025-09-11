package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String userName,
                           @RequestParam String password
                        ) {
        userService.registration(userName, password);
        return "redirect:/login?registered";
    }
    @GetMapping("/login")
    public String login(Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/menu";
        }
        return "login";
    }
    @GetMapping("menu")
    public String menu() {
        return "menu";
    }

}
