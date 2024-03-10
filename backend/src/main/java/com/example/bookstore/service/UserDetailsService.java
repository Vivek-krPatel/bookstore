/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.service;

import com.example.bookstore.DTO.Order_DTO;
import com.example.bookstore.DTO.User_DTO;
import com.example.bookstore.Exception.BookNotFoundException;
import com.example.bookstore.Exception.UserNotFoundException;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.CartProduct;
import com.example.bookstore.models.OrderProduct;
import com.example.bookstore.models.UserDetails;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.OrderRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.bookstore.repository.UserDetailsRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.bookstore.repository.CartProductRepository;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ACE
 */
@Service
@AllArgsConstructor
public class UserDetailsService {
    private final UserDetailsRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderProductService orderProductService;
    
    
    public void createUser(UserDetails user){
        this.userRepository.save(user);
    }
    
    public UserDetails findUserById(Long id){
        Optional<UserDetails> user = userRepository.findById(id);
        if(!user.isPresent()){
            throw new UserNotFoundException(id);
        }
        return user.get();
    }
    
    public Optional<UserDetails> findUserByUsername(String name){
        Optional<UserDetails> user = userRepository.findByUsername(name);
        if(!user.isPresent()){
            throw new UserNotFoundException(name);
        }
        return user;
         
    }

    public Optional<UserDetails> findUserByPhone(String phone){
        Optional<UserDetails> user = this.userRepository.findByPhone(phone);
        if(!user.isPresent()){
            throw new UserNotFoundException(phone);
        }
        return user;
    }
    
    public void deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
    }
    
    public Order_DTO createOrderForUser(Long userId, CartProduct product){
        UserDetails user = userRepository.findById(userId).orElseThrow(() ->
        new UserNotFoundException(userId));
       
        
        //Optional<Book> optional = bookRepository.findById(product.getBook().getId());
       // if(optional.isPresent()){
         //   Book book = optional.get();
            Order order = 
                    //new Order(book,LocalDate.now(),LocalDate.now(),user,book.getPrice());
                    
                    Order.builder()
                          //.products(Arrays.asList(mapCartProductToOrderProduct(product))
                          //.quantity(quantity)
                          .orderDate(LocalDate.now())
                          .user(user)
                          //.amount(product.getQuantity() * product.getBook().getPrice())
                         // .address(address)
                          .build();
            
       //Set<OrderProduct> setProducts = new HashSet<OrderProduct>();
        //setProducts.add(mapCartProductToOrderProduct(product,order));
        
        //order.setProducts(Arrays.asList(orderProduct));
        order = orderRepository.save(order);
        
        //orderProduct.setPrice();
        OrderProduct orderProduct = mapCartProductToOrderProduct(product,order);
        OrderProduct saved = this.orderProductService.saveOrderProduct(orderProduct);
        Set<OrderProduct> products = new HashSet<>();
        products.add(saved);
        order.setProducts(products);
        order.setAmount();
        order = orderRepository.save(order);
        
        //user.addOrder(order);
        Order_DTO orderdto = Order_DTO.builder()
                             .orderId(order.getOrderId())
                             .products(order.getProducts())
                             .orderDate(order.getOrderDate().toString())
                             .status(order.getStatus())
                             .modifiedAt(order.getModifiedAt()!= null?order.getModifiedAt().toString():null)
                             .username(order.getUser().getUsername())
                             .amount(order.getAmount())
                             //.address()
                             .build();
        return orderdto;
       // }else{
       //     throw new BookNotFoundException(product.getBook().getId());
       // }
        
    }
    
    public Order_DTO checkout(Long userId, Cart cart){
        UserDetails user = this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        for(CartProduct product : cart.getProducts()){
            Book book = this.bookRepository.findById(product.getBook().getId()).orElseThrow(() -> new BookNotFoundException(product.getBook().getId()));
        }
        
        Order order = Order.builder()
                    //.products(cart.getProducts())
                    .orderDate(LocalDate.now())
                    .user(user)
                    .build();
        Order blank = orderRepository.save(order);
        
        Set<OrderProduct> orders = cart.getProducts().stream().map(product -> mapCartProductToOrderProduct(product,blank)).collect(Collectors.toSet());
        orders = this.orderProductService.saveAllOrderProducts(orders);
        blank.setProducts(orders);
        blank.setAmount();
        
        Order saved = this.orderRepository.save(blank);
        this.cartService.clearCart(cart.getId());
        
        
        Order_DTO orderdto = Order_DTO.builder()
                    .orderId(saved.getOrderId())
                             .products(saved.getProducts())
                             .orderDate(saved.getOrderDate().toString())
                             .status(saved.getStatus())
                             .modifiedAt(saved.getModifiedAt()!= null?order.getModifiedAt().toString():null)
                             .username(saved.getUser().getUsername())
                             .amount(saved.getAmount())
                             //.address()
                             .build();
        return orderdto;
        
        
    }
    
    //TODO return orderdtos not order entities
    
    public List<Order> getAllOrdersForUser(String username){
        UserDetails user = userRepository.findByUsername(username).orElseThrow(() ->
        new UserNotFoundException(username));
        return user.getOrders();
    }

    public Optional<UserDetails> findUserByEmail(String email) {
         Optional<UserDetails> user = userRepository.findByEmail(email);
        if(!user.isPresent()){
            throw new UserNotFoundException(email);
        }
        return user;
    }
    
    public Cart getUserCart(UserDetails user){
        return this.cartService.findCartByUser(user);
    }
    
    public Set<CartProduct> getCartItemsForUser(Long id){
        return this.cartService.getCartItems(id);
    }
    
    //won't work the exception will be thrown before each checks are performed
    /*
    public Optional<UserDetails> checkForExistingUser(User_DTO dto){
        String email = dto.getEmail();
        String username = dto.getUsername();
        String phone = dto.getPhone();
        
        Optional<UserDetails> user;
        
        try{
            user = this.findUserByEmail(email);
            user = this.findUserByUsername(username);
            user = this.findUserByPhone(phone);
        }catch (UserNotFoundException ex){
            throw new UserNotFoundException(ex.getMessage());
        }
        
        return user;
        
    }
    */
    
    OrderProduct mapCartProductToOrderProduct(CartProduct cartProduct, Order order){
        OrderProduct product = OrderProduct.builder()
                            .book(cartProduct.getBook())
                            .price(cartProduct.getPrice())
                            .quantity(cartProduct.getQuantity())
                            .order(order)
                            .build();
        return product;
        
    }

    
}
