/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    //@JoinColumn(name="product_id")
    private Set<OrderProduct> products;
    
   // private int quantity;
    
    private LocalDate orderDate;
    @Nullable
    private LocalDate modifiedAt;
    
    @Builder.Default
    private OrderStatus status = OrderStatus.PLACED;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private UserDetails user;
    
    private double amount;
    
    @ManyToOne
    @JoinColumn(name="address_id")
    private Address address;
    
    
    
    public double getAmount(){
        return this.amount;
    }
    
    public void setAmount(){
        double sum = 0D;
        for(OrderProduct product : this.getProducts()){
            sum+= product.getPrice();
        }
        
        this.amount = sum;
    }
        
    
}
