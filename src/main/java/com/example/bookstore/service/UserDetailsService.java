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
import com.example.bookstore.models.Order;
import com.example.bookstore.models.UserDetails;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.OrderRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.bookstore.repository.UserDetailsRepository;

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
    
    public Order_DTO createOrderForUser(Long userId, Long bookId){
        UserDetails user = userRepository.findById(userId).orElseThrow(() ->
        new UserNotFoundException(userId));
        
        Optional<Book> optional = bookRepository.findById(bookId);
        if(optional.isPresent()){
            Book book = optional.get();
            Order order = 
                    //new Order(book,LocalDate.now(),LocalDate.now(),user,book.getPrice());
                    
                    Order.builder()
                          .book(book)
                          .orderDate(LocalDate.now())
                          .user(user)
                          .amount(book.getPrice())
                         // .address(address)
                          .build();
     

       // order.setUser(user);
        orderRepository.save(order);
        //user.addOrder(order);
        Order_DTO orderdto = Order_DTO.builder()
                             .orderId(order.getOrderId())
                             .book(book)
                             .orderDate(order.getOrderDate().toString())
                             .modifiedAt(order.getModifiedAt()!= null?order.getModifiedAt().toString():null)
                             .username(order.getUser().getUsername())
                             .amount(order.getAmount())
                             //.address()
                             .build();
        return orderdto;
        }else{
            throw new BookNotFoundException(bookId);
        }
        
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

    
}
