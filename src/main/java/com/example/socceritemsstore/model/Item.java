package com.example.socceritemsstore.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
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

    @ElementCollection
    private List<String> sizes;

}
