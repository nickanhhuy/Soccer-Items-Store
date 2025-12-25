package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @GetMapping
    public String viewAnalytics(Model model) {
        model.addAttribute("stats", analyticsService.getDashboardStats());
        model.addAttribute("recentOrders", analyticsService.getRecentOrders(10));
        model.addAttribute("topCustomers", analyticsService.getTopCustomers(5));
        model.addAttribute("revenueByMonth", analyticsService.getRevenueByMonth());
        return "analytics";
    }
}
