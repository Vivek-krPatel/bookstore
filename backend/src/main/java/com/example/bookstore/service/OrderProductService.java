/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.service;

import com.example.bookstore.Exception.ProductNotFoundException;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.OrderProduct;
import com.example.bookstore.repository.OrderProductRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACE
 */
@Service
@AllArgsConstructor
public class OrderProductService {
    private OrderProductRepository orderProductRepository;
    
    public OrderProduct saveOrderProduct(OrderProduct product){
        return this.orderProductRepository.save(product);
    }
    
    public List<OrderProduct> saveAllOrderProduct(List<OrderProduct> products){
        return this.orderProductRepository.saveAll(products);
    }
    
    public List<OrderProduct> findAllOrderProducts(Order order){
        return this.orderProductRepository.findByOrder(order);
    }
    
    public OrderProduct findOrderProductByBookAndOrder(Order order, Book book){
        return this.orderProductRepository.findByOrderAndBook(order, book).orElseThrow(() -> new ProductNotFoundException(book.getId()));
    }

    public Set<OrderProduct> saveAllOrderProducts(Set<OrderProduct> orders) {
        List<OrderProduct> products =  this.orderProductRepository.saveAll(orders);
        Set<OrderProduct> orderProducts = products.stream().map(item -> item).collect(Collectors.toSet());
        return orderProducts;
    }
    
}
