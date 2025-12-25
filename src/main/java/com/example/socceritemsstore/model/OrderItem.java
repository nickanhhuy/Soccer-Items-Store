package com.example.socceritemsstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String productName;
    
    @NotBlank(message = "Category is required")
    @Pattern(regexp = "^(Boots|Jersey|Others)$", message = "Category must be Boots, Jersey, or Others")
    @Column(nullable = false)
    private String category;
    
    @NotBlank(message = "Size is required")
    @Pattern(regexp = "^(XS|S|M|L|XL|XXL|\\d{2,3})$", message = "Size must be XS, S, M, L, XL, XXL, or shoe size (e.g., 42)")
    @Column(nullable = false)
    private String size;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 99, message = "Quantity cannot exceed 99")
    @Column(nullable = false)
    private Integer quantity;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "9999.99", message = "Price cannot exceed $9999.99")
    @Column(nullable = false)
    private Double price;
    
    @Size(max = 255, message = "Image path cannot exceed 255 characters")
    private String image;
    
    public OrderItem() {}
    
    public OrderItem(String productName, String category, String size, Integer quantity, Double price, String image) {
        this.productName = productName;
        this.category = category;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    @Transient
    public Double getSubtotal() {
        return price != null && quantity != null ? price * quantity : 0.0;
    }
}
