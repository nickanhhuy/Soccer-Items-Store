package com.example.socceritemsstore.dto;

public class UserProfileDTO {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String role;
    private Integer totalOrders;
    private Double totalSpent;
    
    public UserProfileDTO() {}
    
    public UserProfileDTO(Long userId, String username, String email, String fullName, 
                         String phone, String avatarUrl, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Integer getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public Double getTotalSpent() {
        return totalSpent;
    }
    
    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }
}
