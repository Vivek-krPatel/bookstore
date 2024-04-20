/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.DTO;

import java.time.Year;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Vivek
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book_DTO {
    private Long id;
    private String title;
    private String author;
    private float rating;
    private com.example.bookstore.models.Category category;
    private String description;
    private Year Year_of_publication;
    private double price;
    private String image_url;
    
}
