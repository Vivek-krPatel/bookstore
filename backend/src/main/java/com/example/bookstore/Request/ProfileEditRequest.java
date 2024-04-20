/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Vivek
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileEditRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    
}
