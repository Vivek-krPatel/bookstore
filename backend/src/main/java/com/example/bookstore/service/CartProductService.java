/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.service;

import com.example.bookstore.Exception.ProductNotFoundException;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.CartProduct;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.bookstore.repository.CartProductRepository;

/**
 *
 * @author ACE
 */
@Service
@AllArgsConstructor
public class CartProductService {
    private CartProductRepository productRepository;
    
    public CartProduct findProductById(Long id){
        return this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }
    
    public List<CartProduct> findProductsByCart(Cart cart){
        return this.productRepository.findByCart(cart);
    }
    
    public CartProduct saveProduct(Cart cart,CartProduct product){
        Optional<CartProduct> existing = this.productRepository.findByCartAndBook(cart,product.getBook());
        if(existing.isPresent()){
            //System.out.println("book exists" + existing.get());
            if(existing.get().getBook() == product.getBook() && existing.get().getCart() == product.getCart()){
                existing.get().setQuantity(existing.get().getQuantity() + product.getQuantity());
                existing.get().setPrice(existing.get().getPrice() + product.getPrice());
                CartProduct saved = this.productRepository.save(existing.get());
                return saved;
                
            }
        }
        return this.productRepository.save(product);
    }
    
    public CartProduct deleteProduct(Long id){
       CartProduct deleted =  this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
       this.productRepository.deleteById(id);
       return deleted;
    }
    
    public CartProduct findProductByBook(Cart cart,Book book){
        return this.productRepository.findByCartAndBook(cart,book).orElseThrow(() -> new ProductNotFoundException(book));
    }
    
   // public CartProduct setProductQuantity()
    
}
