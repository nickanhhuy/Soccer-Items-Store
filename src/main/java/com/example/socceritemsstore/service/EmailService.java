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
    
    @Value("${spring.mail.from:noreply@husoccershop.store}")
    private String fromEmail;
    
    @Value("${app.name:HuSoccer Shop}")
    private String appName;
    
    public void sendOrderConfirmation(Order order, String customerEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail, appName);  // Set from address with display name
            helper.setTo(customerEmail);
            helper.setSubject("Order Confirmation - " + appName + " #" + order.getId());
            
            String emailContent = buildOrderConfirmationEmail(order);
            helper.setText(emailContent, true);
            
            mailSender.send(message);
            System.out.println("Order confirmation email sent to: " + customerEmail);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // Don't throw exception - email failure shouldn't break order process
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
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
            
            helper.setFrom(fromEmail, appName);  // Set from address with display name
            helper.setTo(email);
            helper.setSubject("Welcome to " + appName + "!");
            
            String emailContent = buildWelcomeEmail(username);
            helper.setText(emailContent, true);
            
            mailSender.send(message);
            System.out.println("Welcome email sent to: " + email);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
    
    private String buildWelcomeEmail(String username) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><style>");
        html.append("body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f8f9fa; margin: 0; padding: 40px 20px; }");
        html.append(".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.07); }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px; text-align: center; }");
        html.append(".header h1 { margin: 0; font-size: 32px; font-weight: 700; }");
        html.append(".content { padding: 40px; }");
        html.append("h2 { color: #2d3748; font-size: 24px; margin: 0 0 16px; }");
        html.append("p { color: #4a5568; font-size: 16px; line-height: 1.6; margin: 0 0 16px; }");
        html.append(".feature-box { background: #f7fafc; border-left: 4px solid #667eea; padding: 20px; margin: 24px 0; border-radius: 8px; }");
        html.append(".feature-box h3 { color: #2d3748; font-size: 18px; margin: 0 0 12px; }");
        html.append(".feature-box ul { margin: 0; padding-left: 20px; color: #4a5568; }");
        html.append(".feature-box li { margin-bottom: 8px; }");
        html.append(".cta-button { display: inline-block; background: #2d3748; color: white !important; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 600; font-size: 16px; margin: 24px 0; }");
        html.append(".footer { background: #f7fafc; padding: 24px; text-align: center; color: #718096; font-size: 12px; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>🎉 Welcome to ").append(appName).append("!</h1>");
        html.append("</div>");
        
        html.append("<div class='content'>");
        html.append("<h2>Hi ").append(username).append(",</h2>");
        html.append("<p>Thank you for verifying your email and joining ").append(appName).append("! We're excited to have you as part of our soccer community.</p>");
        
        html.append("<div class='feature-box'>");
        html.append("<h3>What you can do now:</h3>");
        html.append("<ul>");
        html.append("<li>Browse our premium collection of soccer equipment</li>");
        html.append("<li>Add items to your cart and place orders</li>");
        html.append("<li>Track your order history</li>");
        html.append("<li>Manage your profile and preferences</li>");
        html.append("</ul>");
        html.append("</div>");
        
        html.append("<p>Ready to get started? Click the button below to explore our shop!</p>");
        
        html.append("<div style='text-align: center;'>");
        html.append("<a href='").append(System.getenv("APP_BASE_URL") != null ? System.getenv("APP_BASE_URL") : "http://localhost:8080").append("/menu' class='cta-button'>Start Shopping</a>");
        html.append("</div>");
        
        html.append("<p style='margin-top: 32px;'>If you have any questions, feel free to reach out to our support team.</p>");
        html.append("<p>Happy shopping! ⚽</p>");
        html.append("</div>");
        
        html.append("<div class='footer'>");
        html.append("<p>Thank you for choosing ").append(appName).append("!</p>");
        html.append("<p>&copy; 2024 ").append(appName).append(". All rights reserved.</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
    
    public void sendVerificationEmail(String email, String username, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail, appName);
            helper.setTo(email);
            helper.setSubject("Verify Your Email - " + appName);
            
            String emailContent = buildVerificationEmail(username, token);
            helper.setText(emailContent, true);
            
            mailSender.send(message);
            System.out.println("Verification email sent to: " + email);
            
        } catch (MessagingException e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
    }
    
    private String buildVerificationEmail(String username, String token) {
        // Get base URL from environment or use default
        String baseUrl = System.getenv("APP_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "http://localhost:8080";
        }
        String verificationUrl = baseUrl + "/verify-email?token=" + token;
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><style>");
        html.append("body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f8f9fa; margin: 0; padding: 40px 20px; }");
        html.append(".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.07); }");
        html.append(".icon-circle { width: 80px; height: 80px; background: #d1fae5; border-radius: 50%; margin: 40px auto 24px; display: flex; align-items: center; justify-content: center; }");
        html.append(".icon { font-size: 40px; }");
        html.append(".content { padding: 0 40px 40px; text-align: center; }");
        html.append("h1 { color: #2d3748; font-size: 28px; font-weight: 700; margin: 0 0 16px; }");
        html.append(".subtitle { color: #718096; font-size: 16px; margin: 0 0 8px; }");
        html.append(".email { color: #2d3748; font-weight: 600; font-size: 16px; margin: 0 0 24px; }");
        html.append(".message { color: #4a5568; font-size: 15px; line-height: 1.6; margin: 0 0 16px; }");
        html.append(".spam-notice { color: #718096; font-size: 14px; margin: 0 0 24px; }");
        html.append(".spam-notice strong { color: #2d3748; }");
        html.append(".verify-button { display: inline-block; background: #2d3748; color: white !important; padding: 14px 32px; text-decoration: none; border-radius: 8px; font-weight: 600; font-size: 16px; margin: 8px 0 24px; }");
        html.append(".resend-section { color: #718096; font-size: 14px; margin: 24px 0 0; }");
        html.append(".resend-button { color: #2d3748; text-decoration: none; font-weight: 600; }");
        html.append(".footer { background: #f7fafc; padding: 24px; text-align: center; color: #718096; font-size: 12px; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='icon-circle'>");
        html.append("<span class='icon'>✉️</span>");
        html.append("</div>");
        
        html.append("<div class='content'>");
        html.append("<h1>Please verify your email</h1>");
        html.append("<p class='subtitle'>You're almost there! We sent an email to</p>");
        html.append("<p class='email'>").append(username).append("</p>");
        
        html.append("<p class='message'>Just click on the link in that email to complete your signup. If you don't see it, you may need to <strong>check your spam</strong> folder.</p>");
        
        html.append("<a href='").append(verificationUrl).append("' class='verify-button'>Verify Email Address</a>");
        
        html.append("<p class='resend-section'>Still can't find the email? No problem.</p>");
        html.append("<p class='message'><strong>Note:</strong> This link will expire in 24 hours.</p>");
        html.append("</div>");
        
        html.append("<div class='footer'>");
        html.append("<p>&copy; 2024 ").append(appName).append(". All rights reserved.</p>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
}
