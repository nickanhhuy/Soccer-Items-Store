package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepository) {
        this.userRepo = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        String role = user.getRole();
        System.out.println("DEBUG: Loading user: " + userName + ", Role from DB: " + role);
        
        // Remove ROLE_ prefix if it exists in the database
        String roleWithoutPrefix = role != null ? role.replace("ROLE_", "") : "USER";
        System.out.println("DEBUG: Role after processing: " + roleWithoutPrefix);
        
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .roles(roleWithoutPrefix)
                .build();
    }
}

