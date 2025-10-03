package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@Service
public class UserService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private S3Service s3Service;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private S3Client s3Client;

    // registration
    public void registration(String userName, String password, String email) throws IOException {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("USER");
        User savedUser = userRepo.save(user);

        String userJson = objectMapper.writeValueAsString(savedUser);

        String fileName = "users/" + savedUser.getUser_id() + ".json";
        s3Service.uploadStringAsFile(fileName, userJson);

    }
}
