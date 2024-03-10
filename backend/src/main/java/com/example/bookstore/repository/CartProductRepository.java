/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.repository;

import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.CartProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ACE
 */
@Repository
public interface CartProductRepository extends JpaRepository<CartProduct,Long> {
    
    public List<CartProduct> findByCart(Cart cart);
    
    public Optional<CartProduct> findByCartAndBook(Cart cart, Book book);
    
    
    
}
