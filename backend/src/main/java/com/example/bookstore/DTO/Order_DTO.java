/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.DTO;

import com.example.bookstore.models.Address;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.CartProduct;
import com.example.bookstore.models.OrderProduct;
import com.example.bookstore.models.OrderStatus;
import io.micrometer.common.lang.Nullable;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 *
 * @author Vivek
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order_DTO {
    private Long orderId;
     private Set<OrderProduct> products;
    private String orderDate;
    @Nullable
    private String modifiedAt;
    private OrderStatus status;
    private String username;
    private double amount;
    private Address shipping_adress;
    
    
}
