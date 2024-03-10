/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.repository;

import com.example.bookstore.models.Book;
import com.example.bookstore.models.CartProduct;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.OrderProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ACE
 */
@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {
    
    public List<OrderProduct> findByOrder(Order order);
    
    public Optional<OrderProduct> findByOrderAndBook(Order order, Book book);
    
    
}
