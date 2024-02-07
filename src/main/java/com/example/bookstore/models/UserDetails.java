/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 * @author ACE
 */
@Entity
@NoArgsConstructor
@Data
@Table(name="Users_Details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="first_name",nullable = false)
    private String firstName;
    
    @Column(name="last_name",nullable = false)
    private String lastName;
    
    @Column(name="username",nullable = false, unique = true)
    private String username;
    
    @Column(name="email",nullable = false, unique = true)
    private String email;
    
    @Column(name="encpwd",nullable = false)
    private String password;
    
    @Column(name="phone_no",nullable = false, unique = true)
    private String phone;
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "user", fetch=FetchType.LAZY)
    @Column(name="address",nullable = false)
    @JsonIgnore
    private List<Address> address;
    
    @Column(name="created_at",nullable = false)
    private Date created_at = Date.valueOf(LocalDate.now());
    
    @Column(name="orders")
    @OneToMany(cascade=CascadeType.ALL, mappedBy="user",fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;
    
    //@OneToMany(mappedBy="user")
    //private Set<Book> wishlist;
    
    public void addOrder(Order order){
        this.orders.add(order);
    }
    
    public void deleteOrder(Order order){
        if(this.orders.contains(order)){
            this.orders.remove(order);
        }
    }
        
    public String encrypt_pwd(String pass){
        return " encrypted";
    
    }
     
}
