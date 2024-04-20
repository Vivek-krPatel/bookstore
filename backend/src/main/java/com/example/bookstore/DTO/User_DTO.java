/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.DTO;

import com.example.bookstore.models.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
public class User_DTO {
    @NotNull
    @NotEmpty
    private String firstName;
    
    @NotNull
    @NotEmpty
    private String lastName;
    
    @NotNull
    @NotEmpty
    private String username;
    
    @NotNull
    @NotEmpty
    private String email;
    
    @NotNull
    @NotEmpty
    private String password;
    
    @NotNull
    @NotEmpty
    private String phone;
    
    @NotNull
    @NotEmpty
    private Address address;
    
}
