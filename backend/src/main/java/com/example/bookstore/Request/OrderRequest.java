/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Request;

import com.example.bookstore.models.Address;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.UserDetails;

/**
 *
 * @author Vivek
 * 
 * This will map to order entity and will be saved to order table in the database
 * 
 * Works as a generic order 
 */



public class OrderRequest {
    private UserDetails user;
    private Cart cart;
    private Address address; 
     
}
