package com.example.socceritemsstore.config;

import com.example.socceritemsstore.model.Item;
import com.example.socceritemsstore.model.User;
import com.example.socceritemsstore.repository.ItemRepo;
import com.example.socceritemsstore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if it doesn't exist
        String adminUsername = System.getenv("ADMIN_USERNAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        String adminEmail = System.getenv("ADMIN_EMAIL");
        
        // Use defaults if environment variables are not set (for local development)
        if (adminUsername == null) adminUsername = "admin";
        if (adminPassword == null) adminPassword = "admin123";
        if (adminEmail == null) adminEmail = "admin@husoccer.com";
        
        if (!userRepo.findByUserName(adminUsername).isPresent()) {
            User admin = new User();
            admin.setUserName(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setRole("ADMIN");
            userRepo.save(admin);
            System.out.println("Admin user '" + adminUsername + "' created successfully");
        }

        // Add sample products if database is empty
        if (itemRepo.count() == 0) {
            System.out.println("Adding sample products...");
            
            // Boots
            createItem("Adidas X Speedportal", "Boots", 189.99, 50, "Unisex", "boosts/adidasXspeedPortal.jpg");
            createItem("Adidas X", "Boots", 179.99, 45, "Unisex", "boosts/adidas_x.jpg");
            createItem("Adidas F50 Elite Laceless", "Boots", 199.99, 40, "Unisex", "boosts/F50EliteLaceless.jpg");
            createItem("Adidas F50 League", "Boots", 149.99, 35, "Unisex", "boosts/F50League.jpg");
            createItem("Nike Phantom Elite", "Boots", 189.99, 50, "Unisex", "boosts/nike_phantom_elite.jpg");
            createItem("Nike Phantom 6", "Boots", 179.99, 45, "Unisex", "boosts/nikephantom6.jpg");
            createItem("Nike Phantom GT2", "Boots", 169.99, 40, "Unisex", "boosts/nikephantomgt2.jpg");
            createItem("Nike Tiempo 10", "Boots", 159.99, 35, "Unisex", "boosts/niketiempo10.jpg");
            createItem("Puma Future Z", "Boots", 169.99, 40, "Unisex", "boosts/puma_future_z.jpg");
            createItem("Puma Future 8", "Boots", 159.99, 35, "Unisex", "boosts/pumafuture8.jpg");
            createItem("New Balance Furon 8", "Boots", 149.99, 30, "Unisex", "boosts/newbalanceFuron8.jpg");
            createItem("New Balance Pro V7", "Boots", 139.99, 30, "Unisex", "boosts/newbalanceProV7.jpg");
            createItem("New Balance 7", "Boots", 129.99, 25, "Unisex", "boosts/newbalance7.jpg");
            
            // Jerseys
            createItem("Argentina 2022 World Cup Jersey", "Jersey", 89.99, 100, "Unisex", "jersey/argen22wc.jpg");
            createItem("Arsenal 2025 Jersey", "Jersey", 89.99, 95, "Unisex", "jersey/asernal25.jpg");
            createItem("Barcelona Yamal Jersey", "Jersey", 94.99, 90, "Unisex", "jersey/barcayamal.jpg");
            createItem("Chelsea 2025 Jersey", "Jersey", 89.99, 85, "Unisex", "jersey/chelsea25.jpg");
            createItem("Chelsea 3rd Kit", "Jersey", 89.99, 80, "Unisex", "jersey/chelsea3rd.jpg");
            createItem("Chelsea Retro Jersey", "Jersey", 79.99, 75, "Unisex", "jersey/chelsearetro.jpg");
            createItem("Japan 2022 World Cup Jersey", "Jersey", 89.99, 70, "Unisex", "jersey/japan2022wc.jpg");
            createItem("Liverpool 2025 Jersey", "Jersey", 89.99, 95, "Unisex", "jersey/liverpool25.jpg");
            createItem("Manchester City 2025 Jersey", "Jersey", 89.99, 90, "Unisex", "jersey/mancity25.jpg");
            createItem("Manchester United 2025 Jersey", "Jersey", 89.99, 85, "Unisex", "jersey/mu25.jpg");
            createItem("PSG 2025 Jersey", "Jersey", 89.99, 80, "Unisex", "jersey/psg25.jpg");
            createItem("Spain World Cup 2026 Jersey", "Jersey", 94.99, 75, "Unisex", "jersey/spainWC26.jpg");
            
            // Others
            createItem("Adidas Sala Indoor Ball", "Others", 39.99, 100, "Unisex", "others/adidasSala.jpg");
            createItem("World Cup 2014 Ball", "Others", 49.99, 80, "Unisex", "others/ballWC14.jpg");
            createItem("Training Equipment Set", "Others", 79.99, 50, "Unisex", "others/C12122.jpg");
            createItem("Nike Goalkeeper Gloves", "Others", 49.99, 60, "Unisex", "others/nikeGloves.jpg");
            createItem("Nike Premier League 2024 Ball", "Others", 59.99, 70, "Unisex", "others/nikePL24.jpg");
            createItem("Predator Goalkeeper Gloves", "Others", 54.99, 55, "Unisex", "others/predatorGloves.jpg");
            
            System.out.println("Sample products added successfully!");
        }
    }
    
    private void createItem(String name, String category, double price, int quantity, String gender, String image) {
        Item item = new Item();
        item.setName(name);
        item.setCategory(category);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setGender(gender);
        item.setImage(image);
        itemRepo.save(item);
    }
}
