package com.example.socceritemsstore.dto;

import com.example.socceritemsstore.model.User;

public class UserBackupDTO {
    private Long userId;
    private String userName;
    private String email;
    private String role;
    private String fullName;
    private String phone;
    private String avatarUrl;
    
    public UserBackupDTO(User user) {
        this.userId = user.getUser_id();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.avatarUrl = user.getAvatarUrl();
        // NOTE: Password is intentionally excluded for security
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
