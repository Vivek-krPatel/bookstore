/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author ACE
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
    private String created_at;
    private double price;
    private String image_url;
    
}
