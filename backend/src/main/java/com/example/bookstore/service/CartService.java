/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.service;

import com.example.bookstore.Exception.BookNotFoundException;
import com.example.bookstore.Exception.CartNotFoundException;
import com.example.bookstore.Exception.ProductNotFoundException;
import com.example.bookstore.Request.ProductRequest;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.CartProduct;
import com.example.bookstore.models.UserDetails;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.bookstore.repository.CartProductRepository;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author ACE
 */
@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private BookRepository bookRepository;
    private CartProductRepository cartproductRepository;
    private final CartProductService cartproductService;
    
    public Cart createCart(UserDetails user){
        Cart cart = Cart.builder()
                    .user(user)
                    .products(new HashSet<>())
                    .totalPrice(0.0)
                    .build();
        return this.cartRepository.save(cart);
    }
    
    public Cart findCartByUser(UserDetails user){
        Optional<Cart> cart = this.cartRepository.findByUser(user);
        if(cart.isPresent()){
            return cart.get();
        }else{
            throw new CartNotFoundException(user.getId());
        
        }
    }
    
    public Cart findCartById(Long id){
        Optional<Cart> cart = this.cartRepository.findById(id);
        if(cart.isPresent()){
            return cart.get();
        }else{
             throw new CartNotFoundException(id);
        }
    }
    
    public CartProduct addItemToCart(ProductRequest request,Long cartId){
        Optional<Book> opt_book = this.bookRepository.findById(request.getBookId());
        if(opt_book.isEmpty()){
            throw new BookNotFoundException(request.getBookId());
        }
        
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
        
        CartProduct item = CartProduct.builder()
                    .book(opt_book.get())
                    .quantity(request.getQuantity())
                    .cart(cart)
                    .build();
        item.setPrice();
        
        /*
        
        Optional<Product> existing = this.productRepository.findByBook(item.getBook());
        if(existing.isPresent()){
            if(existing.get().getId() == item.getBook().getId()){
                existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
                existing.get().setPrice(existing.get().getPrice() + item.getPrice());
                CartProduct saved = this.productRepository.save(existing.get());
                cart.setTotalPrice(cart.getTotalPrice() + (item.getQuantity() * opt_book.get().getPrice()));
                this.cartRepository.save(cart);
                return saved;
                
            }
      
        if(cart.getProducts() != null) {
            Optional<Product> existing_in_user = cart.getProducts().stream().filter(cartitem ->
            cartitem.getBook().getId() == item.getBook().getId()).findFirst();
            if(existing.isPresent()){
                existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
            }
        }
        */
        
        CartProduct saved = this.cartproductService.saveProduct(cart,item);
        cart.addProduct(saved);
        cart.setTotalPrice(cart.getTotalPrice() + (saved.getPrice()));
         
        this.cartRepository.save(cart); 
        return saved;
       
    }   
    
    public void removeProductFromCart(Long productId, Long cartId){
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
        CartProduct item = this.cartproductRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        cart.removeProduct(item);
        this.cartproductRepository.deleteById(productId);
        cart.setTotalPrice(cart.getTotalPrice() - (item.getPrice()));
        this.cartRepository.save(cart);
        
    }
    
    public Set<CartProduct> getCartItems(Long cartId){
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        return cart.getProducts();
        //return this.productRepository.findByCart(cart);
    }
    
    public Double getTotalPrice(Long cartId){
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        return cart.getTotalPrice();
    }
    
    public Cart updateCartTotalPrice(Long cartId, Double totalPrice){
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        cart.setTotalPrice(totalPrice);
        return cart;
    } 
    
    /*
    public Cart clearCart(Long cartId){
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        Set<CartProduct> products = cart.getProducts();
        Iterator itr = cart.getProducts().iterator();
        while(itr.hasNext()){
            itr.next();
            itr.remove();  
        }
        cart.getProducts().clear();
        //cart.getProducts().stream().map( product -> this.cartproductRepository.deleteById(product.getId()));
        for (CartProduct product : products){
            //cart.removeProduct(product);
            this.cartproductRepository.deleteById(product.getId());
        }
        
        cart.setTotalPrice(0D);
        cart = this.cartRepository.save(cart);
        return cart;
    }
    */
    
    public Cart clearCart(Long cartId){
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        Set<CartProduct> products = cart.getProducts();
        Iterator<CartProduct> itr = products.iterator();
        while(itr.hasNext()){
            CartProduct product = itr.next();
            itr.remove();
            this.cartproductRepository.deleteById(product.getId());
        }
        cart.getProducts().clear();
        cart.setTotalPrice(0D);
        cart = this.cartRepository.save(cart);
        return cart;
    }
}
