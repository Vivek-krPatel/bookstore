/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.repository;

import com.example.bookstore.models.Cart;
import com.example.bookstore.models.UserDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vivek
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    public Optional<Cart> findByUser(UserDetails user);
    
}
