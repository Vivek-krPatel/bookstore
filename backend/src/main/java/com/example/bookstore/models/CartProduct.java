/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.models;

import com.example.bookstore.DTO.Book_DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 *
 * @author ACE
 */


@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude="cart")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Book book;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;
    
    private int quantity;
    
    private double price;
    
    public double getPrice(){
        return this.book.getPrice() * this.quantity;
    } 
    
    public void setPrice(){
        this.price = this.book.getPrice() * this.quantity;
    }
}
