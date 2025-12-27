package com.example.socceritemsstore.service;

import com.example.socceritemsstore.model.Order;
import com.example.socceritemsstore.model.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.name:HuSoccer Shop}")
    private String appName;
    
    public void sendOrderConfirmation(Order order, String customerEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(customerEmail);
            helper.setSubject("Order Confirmation - " + appName + " #" + order.getId());
            
            String emailContent = buildOrderConfirmationEmail(order);
            helper.setText(emailContent, true);
            
            mailSender.send(message);
            System.out.println("Order confirmation email sent to: " + customerEmail);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // Don't throw exception - email failure shouldn't break order process
        }
    }
    
    private String buildOrderConfirmationEmail(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String orderDate = order.getOrderDate().format(formatter);
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }");
        html.append(".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }");
        html.append(".order-details { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; }");
        html.append(".item { border-bottom: 1px solid #eee; padding: 15px 0; }");
        html.append(".item:last-child { border-bottom: none; }");
        html.append(".total { font-size: 20px; font-weight: bold; color: #667eea; margin-top: 20px; padding-top: 20px; border-top: 2px solid #667eea; }");
        html.append(".footer { text-align: center; color: #666; margin-top: 30px; font-size: 12px; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1> Order Confirmed!</h1>");
        html.append("<p>Thank you for your order at ").append(appName).append("</p>");
        html.append("</div>");
        
        html.append("<div class='content'>");
        html.append("<h2>Hi ").append(order.getFullName()).append(",</h2>");
        html.append("<p>Your order has been successfully placed and is being processed.</p>");
        
        html.append("<div class='order-details'>");
        html.append("<h3>Order Details</h3>");
        html.append("<p><strong>Order ID:</strong> #").append(order.getId()).append("</p>");
        html.append("<p><strong>Order Date:</strong> ").append(orderDate).append("</p>");
        
        html.append("<h3 style='margin-top: 20px;'>Items Ordered:</h3>");
        for (OrderItem item : order.getOrderItems()) {
            html.append("<div class='item'>");
            html.append("<strong>").append(item.getProductName()).append("</strong><br>");
            html.append("Size: ").append(item.getSize()).append(" | ");
            html.append("Quantity: ").append(item.getQuantity()).append(" | ");
            html.append("Price: $").append(String.format("%.2f", item.getPrice())).append("<br>");
            html.append("Subtotal: $").append(String.format("%.2f", item.getPrice() * item.getQuantity()));
            html.append("</div>");
        }
        
        html.append("<div class='total'>");
        html.append("Total Amount: $").append(String.format("%.2f", order.getTotalAmount()));
        html.append("</div>");
        html.append("</div>");
        
        html.append("<div class='order-details'>");
        html.append("<h3>Shipping Information</h3>");
        html.append("<p>").append(order.getFullName()).append("<br>");
        html.append(order.getAddress()).append("<br>");
        html.append(order.getCity()).append(", ").append(order.getState()).append(" ").append(order.getZipCode()).append("<br>");
        html.append("Phone: ").append(order.getPhone()).append("</p>");
        html.append("<p><strong>Payment Method:</strong> ").append(order.getPaymentMethod()).append("</p>");
        html.append("</div>");
        
        html.append("<p>We'll send you another email when your order ships.</p>");
        html.append("<p>If you have any questions, please contact our support team.</p>");
        
        html.append("<div class='footer'>");
        html.append("<p>Thank you for shopping with ").append(appName).append("!</p>");
        html.append("<p>&copy; 2024 ").append(appName).append(". All rights reserved.</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
    
    public void sendWelcomeEmail(String email, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Welcome to " + appName + "!");
            
            String emailContent = buildWelcomeEmail(username);
            helper.setText(emailContent, true);
            
            mailSender.send(message);
            System.out.println("Welcome email sent to: " + email);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
    
    private String buildWelcomeEmail(String username) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }");
        html.append(".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1> Welcome to ").append(appName).append("!</h1>");
        html.append("</div>");
        
        html.append("<div class='content'>");
        html.append("<h2>Hi ").append(username).append(",</h2>");
        html.append("<p>Thank you for registering with ").append(appName).append("!</p>");
        html.append("<p>You can now browse our collection of premium soccer equipment and place orders.</p>");
        html.append("<p>Happy shopping!</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
}
