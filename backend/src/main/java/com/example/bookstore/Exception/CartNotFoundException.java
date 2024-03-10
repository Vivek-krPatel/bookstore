/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Exception;

/**
 *
 * @author ACE
 */
public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(Long id) {
        super("Cart not found for user with id :" + id);
    }
    
    
}
