package com.example.socceritemsstore.dto;

import java.time.LocalDateTime;

public class OrderStatisticsDTO {
    private Long totalOrders;
    private Double totalRevenue;
    private Double averageOrderValue;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    
    public OrderStatisticsDTO() {}
    
    public OrderStatisticsDTO(Long totalOrders, Double totalRevenue, Double averageOrderValue, 
                             LocalDateTime periodStart, LocalDateTime periodEnd) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.averageOrderValue = averageOrderValue;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }
    
    public Long getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public Double getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public Double getAverageOrderValue() {
        return averageOrderValue;
    }
    
    public void setAverageOrderValue(Double averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }
    
    public LocalDateTime getPeriodStart() {
        return periodStart;
    }
    
    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }
    
    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }
    
    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }
}
