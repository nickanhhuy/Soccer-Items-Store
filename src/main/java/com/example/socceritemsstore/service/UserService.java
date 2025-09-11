package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // registration
    public void registration(String userName, String password) {
        System.out.println("Saving user: " + userName);
        User user = new User();
        user.setUsername(userName);
        user.setPassword(passwordEncoder.encode(password));
        User saved = userRepo.save(user);
        System.out.println("Saved user: " + saved.getUser_id());

    }
}
