package com.example.socceritemsstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }// encrypt the password for high security

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/css/**", "/js/**", "/static/image/**", "/image/**").permitAll()
                    .requestMatchers("/", "/menu").permitAll() // Allow public access to home and menu
                    .requestMatchers("/register", "/login").permitAll()
                    .requestMatchers("/verify-email").permitAll() // Allow email verification without login
                    .requestMatchers("/test-email*", "/test-email-form*").permitAll() // Allow email testing
                    .requestMatchers("/admin", "/admin/**").hasRole("ADMIN") // admin page access: adding, updating or delete items
                    .requestMatchers("/analytics", "/analytics/**").hasRole("ADMIN") // Analytics dashboard for admin only
                    .requestMatchers("/api/s3/**").hasRole("ADMIN") // S3 API endpoints - ADMIN ONLY
                    .requestMatchers("/order", "/receipt", "/history", "/checkout").hasAnyRole("USER", "ADMIN")//authorized users can access to those pages
                    .requestMatchers("/profile", "/profile/**").hasAnyRole("USER", "ADMIN") // Profile management
                    .anyRequest().authenticated(); // All other requests require authentication
        }).formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")  // Use email parameter instead of username
                .passwordParameter("password")
                .defaultSuccessUrl("/menu", true)  //if login successfully it will direct to menu page
                .failureUrl("/login?error")//failure cases
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/menu")  // After logout, redirect to menu instead of login
                .permitAll()
        ).userDetailsService(customUserDetailsService);  // Use custom UserDetailsService
        return http.build();
    }
}
