package com.example.socceritemsstore.security;

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

    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }// encrypt the password for high security

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/css/**", "/js/**", "/image/**").permitAll()
                    .requestMatchers("/register", "/login").permitAll()
                    .requestMatchers("/admin").hasRole("ADMIN") // admin page access: adding, updating or delete items
                    .requestMatchers("/order", "/receipt", "history").hasAnyRole("USER", "ADMIN")//authorized users can access to those pages
                    .anyRequest().authenticated(); // All other requests require authentication
        }).formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/menu", true)  //if login successfully it will direct to menu page
                .failureUrl("/login?failed")//failure cases
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
        );
        return http.build();
    }
}
