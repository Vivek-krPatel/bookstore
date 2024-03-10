/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.controller;


import com.example.bookstore.DTO.Address_DTO;
import com.example.bookstore.DTO.Order_DTO;
import com.example.bookstore.DTO.User_DTO;
import com.example.bookstore.Exception.AddressNotFoundException;
import com.example.bookstore.Exception.CartNotFoundException;
import com.example.bookstore.Exception.UserAlreadyExistsException;
import com.example.bookstore.Exception.UserNotFoundException;
import com.example.bookstore.Request.LoginRequest;
import com.example.bookstore.models.Address;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.CartProduct;
import com.example.bookstore.models.UserDetails;
import com.example.bookstore.repository.AddressRepository;
import com.example.bookstore.response.UserProfileResponse;
import com.example.bookstore.service.AddressService;
import com.example.bookstore.service.CartService;
import com.example.bookstore.service.CartProductService;
import com.example.bookstore.service.UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

public class UserDetailsController {
 
    private final UserDetailsService userService;
    private final AddressService addressService;
    private final CartService cartService;
    private final CartProductService productService;
    
    private final ModelMapper mapper;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JdbcUserDetailsManager manager;
    
    private AuthenticationManager authenticationManager;
    
    private SecurityContextRepository repo = new HttpSessionSecurityContextRepository();
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public UserDetailsController(UserDetailsService userService, CartService cartService,CartProductService productService, AddressService addressService, ModelMapper mapper, PasswordEncoder passwordEncoder, JdbcUserDetailsManager manager, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.addressService = addressService;
        this.cartService = cartService;
        this.productService = productService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.manager = manager;
        this.authenticationManager = authenticationManager;
    }

    
   
    
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
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authenticationResponse);
            this.securityContextHolderStrategy.setContext(context);
            this.repo.saveContext(context, request, response);

            //SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
            return ResponseEntity.ok("log in successfull");
           } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
           }
        
    }

/*
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
        
        String token = jwtTokenProvider.generateToken(userDetails);
        
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
        
        */
    private SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication auth, HttpServletRequest request, HttpServletResponse response){
       // HttpSession session = request.getSession(false);
       // if(session != null){
        //    session.invalidate();
       // }
        this.handler.logout(request, response, auth);
        return ResponseEntity.noContent().build();
    }
    

    
    @PostMapping("/register")
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
    
    @GetMapping("/mycart")
    ResponseEntity<?> getCart(Authentication auth){
         String username = auth.getName();
         UserDetails user = this.userService.findUserByUsername(username).get();
         Cart cart = this.cartService.findCartByUser(user);
         //return ResponseEntity.ok(cart);
         Set<CartProduct> products = this.cartService.getCartItems(cart.getId());
        return ResponseEntity.ok(products);
    }
    
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication auth){
        if(auth instanceof AnonymousAuthenticationToken){
             return new ResponseEntity<>("Please Log in",HttpStatus.UNAUTHORIZED);
        }
        
        Long id;
        UserDetails user;
        try{
        String name = auth.getName();
        user = this.userService.findUserByUsername(name).get();
        id = userService.findUserByUsername(name).get().getId();
        } catch(UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
        
        try{
            Cart cart = this.cartService.findCartByUser(user);
            Order_DTO orderDTO = this.userService.checkout(id, cart);
            /*
            List<Order_DTO> orders = cart.getProducts().stream().map( item -> 
                                   this.userService.createOrderForUser(id, item.getBook().getId(),item.getQuantity()))
                                    .collect(Collectors.toList());
            */
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
        }catch (CartNotFoundException ex){
             throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
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
                .products(order.getProducts())
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
