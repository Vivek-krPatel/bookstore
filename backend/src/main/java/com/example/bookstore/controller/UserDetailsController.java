/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.controller;


import com.example.bookstore.DTO.Address_DTO;
import com.example.bookstore.DTO.Order_DTO;
import com.example.bookstore.DTO.User_DTO;
import com.example.bookstore.Exception.AddressNotFoundException;
import com.example.bookstore.Exception.UserAlreadyExistsException;
import com.example.bookstore.Exception.UserNotFoundException;
import com.example.bookstore.models.Address;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.UserDetails;
import com.example.bookstore.repository.AddressRepository;
import com.example.bookstore.response.UserProfileResponse;
import com.example.bookstore.service.AddressService;
import com.example.bookstore.service.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 *
 * @author ACE
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserDetailsController {
 
    private final UserDetailsService userService;
    private final AddressService addressService;
    
    private final ModelMapper mapper;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JdbcUserDetailsManager manager;
    
    
    @GetMapping("/principal")
    public ResponseEntity<String> getPrincipal(Authentication authentication){
        Object principal =  authentication.getPrincipal();
        String name = authentication.getName();
        Object cred  = authentication.getCredentials();
        
        return ResponseEntity.ok("principal :" + principal + "\n" + "name : " + name + "\n" + "cred :" + cred);
        
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> findUser(Authentication auth){
        if(auth instanceof AnonymousAuthenticationToken){
            return new ResponseEntity<>("Please Log in",HttpStatus.UNAUTHORIZED);
        }
         try{
             String username = auth.getName();
             UserDetails user = this.userService.findUserByUsername(username).get();
             UserProfileResponse profile = mapper.map(user, UserProfileResponse.class);
        return ResponseEntity.ok(profile);
        } catch(UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
    }
    
    @PostMapping("/register")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@RequestBody User_DTO userdto){
        try{
            Optional<UserDetails> existing_email = userService.findUserByEmail(userdto.getEmail());
            
            if(existing_email.isPresent()){
                throw new UserAlreadyExistsException(userdto.getEmail());
                 }
            
            }catch(UserNotFoundException ex){
                
                // I don't know what to do with this catch bock.  
            }

        try{
            Optional<UserDetails> existing_phone = userService.findUserByPhone(userdto.getPhone());

            if(existing_phone.isPresent()){
                throw new UserAlreadyExistsException(userdto.getPhone());
            }

        }catch(UserNotFoundException ex){

            // I don't know what to do with this catch bock either.
        }

        try{
            Optional<UserDetails> existing_username = userService.findUserByUsername(userdto.getUsername());
            if(existing_username.isPresent()){
                    throw new UserAlreadyExistsException(userdto.getUsername());
                }
        }catch(UserNotFoundException ex){   
            UserDetails newuser = mapper.map(userdto, UserDetails.class);
            this.userService.createUser(newuser);
            
        
       // List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
       // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
            String username = newuser.getUsername();
            String password = newuser.getPassword();
        
            org.springframework.security.core.userdetails.UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        
        //UserDetails user = new User(username, passwordEncoder.encode(password), authorities);
        
            manager.createUser(user);
        }
        
            return ResponseEntity.ok().build();
       
    }

    //TODO return orderdtos not order entities
    
    @GetMapping("/orders")
    public ResponseEntity<List<Order_DTO>> getAllOrdersForUser(Authentication auth){
        String username = auth.getName();
        List<Order> orders = userService.getAllOrdersForUser(username);
        List<Order_DTO> orderdtos = orders.stream().map(this::orderDtoMapper)
                                   .collect(Collectors.toList());;
        return ResponseEntity.ok(orderdtos);
    }

    
    
    @PostMapping("/profile/address")
    public ResponseEntity<?> addAddress(@RequestBody Address_DTO dto, Authentication auth){
        
        String name = auth.getName();
        UserDetails userId = this.userService.findUserByUsername(name).get();
        
        Address address = Address.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .streetAddress(dto.getStreetAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .user(userId)
                .build();
        
        this.addressService.saveAddressForUser(address);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
            
    }
    
    @GetMapping("/profile/address")
    public ResponseEntity<?> getAddress(Authentication auth){
        String name = auth.getName();
        Long user = this.userService.findUserByUsername(name).get().getId();
        
        try{
            Optional<Address> address = this.addressService.getAddressForUser(user);
                return ResponseEntity.ok(address);
        }catch (AddressNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        } 
    }
    
    
    public Order_DTO orderDtoMapper(Order order){
        Order_DTO dto = Order_DTO.builder()
                .orderId(order.getOrderId())
                .book(order.getBook())
                .orderDate(order.getOrderDate().toString())
                .modifiedAt(order.getModifiedAt()!= null?order.getModifiedAt().toString():null)
                .username(order.getUser().getUsername())
                .amount(order.getAmount())
                .build();

        return dto;
    }
    
    
    // May be better in book controller 
    
    /*
    
    
    @PostMapping("/books/{bookId}")
    public ResponseEntity<OrderResponse> createOrderForUser(Authentication auth,@PathVariable Long bookId, UriComponentsBuilder uriComponentsBuilder){
        
        String name = auth.getName();
        
        try{
        UserDetails user = this.userService.findUserByUsername(name).get();
        } catch(UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
        
        Long userId = this.userService.findUserByUsername(name).get().getId();
        Order_DTO orderdto = userService.createOrderForUser(userId, bookId);
        String orderUrl = uriComponentsBuilder.path("/users/"+userId+"/orders/"+orderdto.getOrderId())
                            .toUriString();
        OrderResponse orderResponse = new OrderResponse(orderdto,orderUrl); 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(orderUrl));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        
    }
    
    
    @GetMapping("/books")
    public ResponseEntity<CollectionModel<EntityModel<Book_DTO>>> all(Authentication auth){
        //CollectionModel<EntityModel<Book>> collectionModel = bookController.all();
        return bookController.all();  
    }
    
    @GetMapping("/books/{book_id}")
    @ResponseBody
    public ResponseEntity<EntityModel<Book_DTO>> one(Authentication auth, @PathVariable Long book_id){
        try{
        String name = auth.getName();
        UserDetails user = this.userService.findUserByUsername(name).get();
        } catch(UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
        return bookController.one(book_id);
    }

    */
    
}
