package com.example.socceritemsstore.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import javax.annotation.processing.Generated;
import java.util.List;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "Item name is required")
    public String name;
    @NotBlank(message = "Category is required")
    public String category;
    public Integer quantity;
    public Double price;
    public String gender;
    public String imageURL;
    @ElementCollection
    private List<String> sizes;
    public Item() {}

    // Constructor with fields
    public Item(String name, String category, Integer quantity, Double price, String gender, String imageURL, List<String> sizes) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.gender = gender;
        this.imageURL = imageURL;
        this.sizes = sizes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }
}
