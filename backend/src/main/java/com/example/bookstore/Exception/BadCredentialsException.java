/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Exception;

/**
 *
 * @author ACE
 */
public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException() {
        super("Username or password is incorrect");
    }
    
}
