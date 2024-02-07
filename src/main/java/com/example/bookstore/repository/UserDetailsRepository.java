/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.bookstore.repository;

import com.example.bookstore.models.UserDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ACE
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails,Long>{
    
    Optional<UserDetails> findByEmail(String email);
    Optional<UserDetails> findByUsername(String username);

    Optional<UserDetails> findByPhone(String phone);
}
