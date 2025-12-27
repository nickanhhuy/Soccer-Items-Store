package com.example.socceritemsstore.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;


    public void uploadStringAsFile(String key, String jsonContent) throws IOException {
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("application/json")
                        .build(),
                RequestBody.fromString(jsonContent, StandardCharsets.UTF_8)
        );
    }
    
    // Upload backup with encryption
    public void uploadBackup(String key, String jsonContent) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType("application/json")
                            .serverSideEncryption("AES256")
                            .build(),
                    RequestBody.fromString(jsonContent, StandardCharsets.UTF_8)
            );
            System.out.println("Backup uploaded successfully: " + key);
        } catch (Exception e) {
            System.err.println("Failed to upload backup to S3: " + e.getMessage());
            // Don't fail the operation if backup fails
        }
    }

    public byte[] downloadFile(String key) {
        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        return objectAsBytes.asByteArray();
    }
    
    // Download backup
    public byte[] downloadBackup(String key) {
        try {
            ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            );
            return objectAsBytes.asByteArray();
        } catch (Exception e) {
            System.err.println("Failed to download backup from S3: " + e.getMessage());
            return null;
        }
    }

}
