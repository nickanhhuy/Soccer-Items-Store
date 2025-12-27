package com.example.socceritemsstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${upload.path:uploads/avatars}")
    private String uploadPath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        String uploadDirPath = uploadDir.toUri().toString();
        
        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations(uploadDirPath + "/");
    }
}
