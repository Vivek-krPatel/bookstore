/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;
import java.time.Year;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author ACE
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="title",nullable = false)
    private String title;
    
    @Column(name="author",nullable = false)
    private String author;
    
    @Column(name="rating")
    private float rating;
    
    @Column(name="category")
    private Category category;
    
    @Column(name="description")
    private String description;
    
    @Column(name="Year_of_publication")
    private Year Year_of_publication;
    
    @Column(name="price",nullable = false)
    private double price;
    
    @Column(name="image_url")
    private String image_url; 

    public Book(String title, String author, float rating, Category category, String description, Year Year_of_publication, double price, String image_url) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.category = category;
        this.description = description;
        this.Year_of_publication = Year_of_publication;
        this.price = price;
        this.image_url = image_url;
    }
    
    
    
     
}
