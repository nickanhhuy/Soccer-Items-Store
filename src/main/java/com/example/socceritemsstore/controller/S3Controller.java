package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    // ADMIN ONLY - Upload JSON to S3
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload-json/{key}")
    public ResponseEntity<String> uploadJson(
            @PathVariable String key,
            @RequestBody String jsonContent) {
        try {
            // Validate key to prevent path traversal
            if (key.contains("..") || key.startsWith("/")) {
                return ResponseEntity.badRequest().body("Invalid key format");
            }
            
            s3Service.uploadStringAsFile(key, jsonContent);
            return ResponseEntity.ok("JSON uploaded to S3 successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload: " + e.getMessage());
        }
    }
    
    // ADMIN ONLY - Download file from S3
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String key) {
        try {
            // Validate key to prevent path traversal
            if (key.contains("..") || key.startsWith("/")) {
                return ResponseEntity.badRequest().body(null);
            }
            
            byte[] data = s3Service.downloadFile(key);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    
    // ADMIN ONLY - Download backup
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/backup/{key}")
    public ResponseEntity<byte[]> downloadBackup(@PathVariable String key) {
        try {
            // Validate key to prevent path traversal
            if (key.contains("..") || key.startsWith("/")) {
                return ResponseEntity.badRequest().body(null);
            }
            
            // Only allow downloading from users/ folder
            if (!key.startsWith("users/")) {
                return ResponseEntity.badRequest().body(null);
            }
            
            byte[] data = s3Service.downloadBackup(key);
            if (data == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
