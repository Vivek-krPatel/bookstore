/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Vivek
 */
@AllArgsConstructor
@Data
public class LoginRequest {
    private String username;
    private String password;
    
}