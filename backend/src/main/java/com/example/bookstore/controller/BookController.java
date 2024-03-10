/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.controller;

import com.example.bookstore.DTO.Book_DTO;
import com.example.bookstore.DTO.Order_DTO;
import com.example.bookstore.Exception.BookNotFoundException;
import com.example.bookstore.Exception.UserNotFoundException;
import com.example.bookstore.Exception.CartNotFoundException;
import com.example.bookstore.Request.ProductRequest;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.Cart;
import com.example.bookstore.models.CartProduct;
import com.example.bookstore.models.UserDetails;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CartService;
import com.example.bookstore.service.CartProductService;
import com.example.bookstore.service.UserDetailsService;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;



/**
 *
 * @author ACE
 */
@RestController
@CrossOrigin()
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService service;
    private final UserDetailsService userService;
    private final CartService cartService;
    private final BookService bookService;
    private final CartProductService productService;
   
    private ModelMapper mapper;
    
    /*
    @Autowired
    public BookController(BookService service){
        this.service = service;
    }
    */
    
    
    @GetMapping
    public  ResponseEntity<CollectionModel<EntityModel<Book>>> all(Authentication auth){
        List<Book> books = this.service.findall();
        List<EntityModel<Book>> bookModels = books.stream().map( book ->
                EntityModel.of(book,linkTo(methodOn(BookController.class).one(auth, book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all(auth))
                        .withRel("books")))
                .collect(Collectors.toList());
        
        Link selfLink = linkTo(methodOn(BookController.class).all(auth)).withSelfRel();
        
        CollectionModel<EntityModel<Book>> collectionModel = CollectionModel.of(bookModels,selfLink);
        return ResponseEntity.ok(collectionModel);  
    }
    
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EntityModel<Book>> one(Authentication auth, @PathVariable Long id){
        try{
        Book book = this.service.findone(id);
        EntityModel<Book> bookModel = EntityModel.of(book);
        Link selfLink = linkTo(methodOn(BookController.class).one(auth, id)).withSelfRel();
        //Link orderBookLink = linkTo(methodOn(BookController.class).createOrderForUser(auth, id)).withRel("place_order");
        Link booksLink = linkTo(methodOn(BookController.class).all(auth)).withRel("books");
        
        bookModel.add(selfLink, booksLink);
        return ResponseEntity.ok(bookModel);
        } catch(BookNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
    }
    
    
    /*
    @PostMapping("/books/{bookId}/add")
    public ResponseEntity<?> addItemtoCart(Authentication auth,CartProduct item){
        if(auth instanceof AnonymousAuthenticationToken){
             return new ResponseEntity<>("Please Log in",HttpStatus.UNAUTHORIZED);
        }
        UserDetails user;
        try{
        String name = auth.getName();
        user = userService.findUserByUsername(name).get();
        Cart cart = this.cartService.findCartByUser(user);
        this.cartService.additemToCart(item, cart.getId());
        return ResponseEntity.ok(cart);
        } catch(UserNotFoundException | CartNotFoundException ex) {
            if(ex instanceof CartNotFoundException){
                Cart cart = this.cartService.createCart(user);
                this.cartService.additemToCart(item, cart.getId());
                return ResponseEntity.ok(cart);
            }else if(ex instanceof UserNotFoundException){   
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
            }
        }
           
    }
    
    */
    
    @PostMapping("/{bookId}/add")
    public ResponseEntity<?> addItemToCart(Authentication auth, @RequestBody ProductRequest request) {
        //UserDetails user;
        try {
            String name = auth.getName();
            UserDetails user = userService.findUserByUsername(name).get();
            try{
                Cart cart = cartService.findCartByUser(user);
                
                CartProduct product = this.cartService.addItemToCart(request, cart.getId());
                return ResponseEntity.ok(product);
            }catch (CartNotFoundException ex){
                Cart cart = this.cartService.createCart(user);
                CartProduct product =this.cartService.addItemToCart(request,cart.getId());
                return ResponseEntity.ok(product);
            }
        } catch (UserNotFoundException ex) {
           // ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
            //return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        //return ResponseEntity.badRequest().build();
    }
    
    //@PreAuthorize("hasRole('USER')")
                                    
                                
        //Order_DTO orderdto = userService.createOrderForUser(id, bookId);
      
       // String orderUrl = uriComponentsBuilder.path("/users/"+userId+"/orders/"+orderdto.getOrderId())
                     //       .toUriString();
      //  OrderResponse orderResponse = new OrderResponse(orderdto,orderUrl); 
      // HttpHeaders headers = new HttpHeaders();
      //  headers.setLocation(URI.create(orderUrl));  
    
    
    /*
     * create a seperate method that checks whether requested item already exits in the users cart,
     * if so then return a exception , product already exists in the cart , and then adjust he quantity accordingly
     * calling another method controlled by user actions , then place order.
     * Also duplicate key error was because i wasn't checking whether the produc already exists in the users cart.
    */
    
    @PostMapping("/{bookid}/buy")
    public ResponseEntity<?> buyBook(Authentication auth,@RequestBody ProductRequest request){
        if(auth instanceof AnonymousAuthenticationToken){
             return new ResponseEntity<>("Please Log in",HttpStatus.UNAUTHORIZED);
        }
        
        try {
            String name = auth.getName();
            UserDetails user = userService.findUserByUsername(name).get();
            try{
                Book book = this.bookService.findone(request.getBookId());
                try{
                    Cart cart = this.cartService.findCartByUser(user);
                    CartProduct product = this.cartService.addItemToCart(request, cart.getId());
                    /*
                    CartProduct product = CartProduct.builder()
                                        .book(book)
                                        .cart(cart)
                                        .quantity(request.getQuantity())
                                        .build();
                    product.setPrice();
                    CartProduct saved = this.productService.saveProduct(product);
                    */
                    Order_DTO orderDTO = this.userService.createOrderForUser(user.getId(), product);
                    return ResponseEntity.ok(orderDTO);
                    }catch (CartNotFoundException ex){
                        Cart cart = this.cartService.createCart(user);
                        CartProduct product = this.cartService.addItemToCart(request,cart.getId());
                        /*
                        CartProduct product = CartProduct.builder()
                                        .book(book)
                                        .cart(cart)
                                        .quantity(request.getQuantity())
                                        .build();
                        product.setPrice();
                        CartProduct saved = this.productService.saveProduct(product);
                        */
                        Order_DTO orderDTO = this.userService.createOrderForUser(user.getId(), product);
                        return ResponseEntity.ok(orderDTO);
            }
                }catch (BookNotFoundException ex){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
                }
            
        } catch (UserNotFoundException ex) {
           // ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
           // return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    /* Admin routes
    * Need refactoring 
    */
  
    
    
   // private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Date parseDate(String date) {
            Date parsed = Date.valueOf(date);
            return parsed;
    }
        
    
    
}
