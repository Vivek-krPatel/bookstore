/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Exception;

/**
 *
 * @author Vivek
 */
public class UserAlreadyExistsException extends RuntimeException{
    
    public UserAlreadyExistsException(String param){
        super("User already exists with : " + param);
    }
    
}
