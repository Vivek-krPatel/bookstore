/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Exception;

/**
 *
 * @author Vivek
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found with id "+ id);
    }
    
    public UserNotFoundException(String param) {
        super("User not found with  "+ param);
    }
    
    
}
