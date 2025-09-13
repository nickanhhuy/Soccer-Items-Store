package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.service.UserService;
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
    //Register Endpoint
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String userName,
                           @RequestParam String password,
                           @RequestParam String email
                        ) {
        userService.registration(userName, password, email);
        return "redirect:/login?registered";
    }
    //Login endpoint
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    //Logout: the user session will be cleared
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // clear the user is in the session
        return "redirect:/login?logout";
    }


}
