/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Exception;

/**
 *
 * @author ACE
 */
public class AddressNotFoundException extends RuntimeException {
    
    public AddressNotFoundException(Long id){
        super("Address not fouund for id : " + id);
    }
    
    
}
