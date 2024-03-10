/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author ACE
 */
@Entity
@Table(name="cart")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
   // @OneToMany(cascade=CascadeType.ALL, mappedBy="user",fetch=FetchType.LAZY)
    @OneToMany(mappedBy="cart",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<CartProduct> products;
    
    @OneToOne
    @JsonIgnore
    private UserDetails user;
    
    private Double totalPrice;
    
    @Transactional
    public void addProduct(CartProduct product){
        this.products.add(product);
    }
    
    @Transactional
    public void removeProduct(CartProduct product){
        if(this.products.contains(product)){
            this.products.remove(product);
        }
    }
    
    
}
