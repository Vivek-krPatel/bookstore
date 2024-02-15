/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.response;

import com.example.bookstore.DTO.Order_DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author ACE
 */
@Data
@AllArgsConstructor
public class OrderResponse {
    private Order_DTO order;
    private String orderUrl;
    
    
    
}
