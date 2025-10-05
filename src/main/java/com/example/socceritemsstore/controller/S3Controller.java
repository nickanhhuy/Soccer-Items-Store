package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.service.S3Service;
import com.example.socceritemsstore.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload-json/{key}")
    public ResponseEntity<String> uploadJson(
            @PathVariable String key,
            @RequestBody String jsonContent) {
        try {
            s3Service.uploadStringAsFile(key, jsonContent);
            return ResponseEntity.ok("JSON uploaded to S3 successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload: " + e.getMessage());
        }
    }
//    @GetMapping("/download/{key}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable String key) {
//        try {
//            byte[] data = s3Service.downloadFile(key);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
//                    .body(data);
//        } catch (Exception e) {
//            return ResponseEntity.status(404).body(null);
//        }
//    }
}
