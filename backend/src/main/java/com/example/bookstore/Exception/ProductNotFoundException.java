/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Exception;

import com.example.bookstore.models.Book;

/**
 *
 * @author Vivek
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product not found with id : " + id);
    }

    public ProductNotFoundException(Book book) {
        super("product not found with book id : " + book.getId());
    }
    
    
    
    
}
