/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Vivek
 */
@Configuration
public class ApplicationConfig {
    
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }
    
}
