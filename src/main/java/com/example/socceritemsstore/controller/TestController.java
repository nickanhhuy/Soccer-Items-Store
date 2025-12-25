package com.example.socceritemsstore.controller;

import com.example.socceritemsstore.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping("/test-email")
    @ResponseBody
    public String testEmail(@RequestParam(defaultValue = "test@example.com") String email,
                           @RequestParam(defaultValue = "TestUser") String username) {
        try {
            emailService.sendWelcomeEmail(email, username);
            return "✅ Test email sent successfully to: " + email;
        } catch (Exception e) {
            // Return more detailed error information
            String errorDetails = "❌ Failed to send test email: " + e.getMessage();
            if (e.getCause() != null) {
                errorDetails += "\nCause: " + e.getCause().getMessage();
            }
            return errorDetails;
        }
    }
    
    @GetMapping("/test-email-form")
    @ResponseBody
    public String testEmailForm() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Email Test</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; background: #f5f5f5; }
                    .container { max-width: 500px; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    input, button { padding: 12px; margin: 8px 0; width: 100%; box-sizing: border-box; border: 1px solid #ddd; border-radius: 5px; }
                    button { background: #007bff; color: white; border: none; cursor: pointer; font-size: 16px; }
                    button:hover { background: #0056b3; }
                    .links { margin-top: 20px; padding-top: 20px; border-top: 1px solid #eee; }
                    .links a { display: block; margin: 5px 0; color: #007bff; text-decoration: none; }
                    .links a:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>🧪 Email Service Test</h2>
                    <p>Test the email functionality without registering a user.</p>
                    
                    <form action="/test-email" method="get">
                        <label><strong>Email Address:</strong></label>
                        <input type="email" name="email" placeholder="Enter email to test" required>
                        
                        <label><strong>Username:</strong></label>
                        <input type="text" name="username" placeholder="Test username" value="TestUser">
                        
                        <button type="submit">Send Test Email</button>
                    </form>
                    
                    <div class="links">
                        <p><strong>Quick Test Links:</strong></p>
                        <a href="/test-email?email=nickayanhhuy@gmail.com&username=TestUser">📧 Test with nickayanhhuy@gmail.com</a>
                        <a href="/test-email?email=test@gmail.com&username=TestUser">📧 Test with test@gmail.com</a>
                        <a href="/menu">🏠 Back to Main App</a>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}