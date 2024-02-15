/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.controller;

import com.example.bookstore.DTO.Book_DTO;
import com.example.bookstore.DTO.Order_DTO;
import com.example.bookstore.Exception.BookNotFoundException;
import com.example.bookstore.Exception.UserNotFoundException;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.UserDetails;
import com.example.bookstore.service.BookService;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;



/**
 *
 * @author ACE
 */
@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
    private final BookService service;
    private final UserDetailsService userService;
   
    private ModelMapper mapper;
    
    /*
    @Autowired
    public BookController(BookService service){
        this.service = service;
    }
    */
    
    
    @GetMapping
    public  ResponseEntity<CollectionModel<EntityModel<Book_DTO>>> all(Authentication auth){
        List<Book> books = this.service.findall();
        List<EntityModel<Book_DTO>> bookModels = books.stream().map( book ->
                                             EntityModel.of(mapper.map(book, Book_DTO.class),linkTo(methodOn(BookController.class)
                                             .one(auth, book.getId()))
                                             .withSelfRel(),
                                              linkTo(methodOn(BookController.class).all(auth))
                                              .withRel("books")))
                                              .collect(Collectors.toList());
        
        Link selfLink = linkTo(methodOn(BookController.class).all(auth)).withSelfRel();
        
        CollectionModel<EntityModel<Book_DTO>> collectionModel = CollectionModel.of(bookModels,selfLink);
        return ResponseEntity.ok(collectionModel);  
    }
    
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EntityModel<Book_DTO>> one(Authentication auth, @PathVariable Long id){
        try{
        Book book = this.service.findone(id);
        EntityModel<Book_DTO> bookModel = EntityModel.of(mapper.map(book,Book_DTO.class));
        Link selfLink = linkTo(methodOn(BookController.class).one(auth, id)).withSelfRel();
        Link orderBookLink = linkTo(methodOn(BookController.class).createOrderForUser(auth, id)).withRel("place_order");
        Link booksLink = linkTo(methodOn(BookController.class).all(auth)).withRel("books");
        
        bookModel.add(selfLink, orderBookLink, booksLink);
        return ResponseEntity.ok(bookModel);
        } catch(BookNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
    }
    
    
    
    
    @PostMapping("/{bookId}/order")
    public ResponseEntity<?> createOrderForUser(Authentication auth, @PathVariable Long bookId){
        if(auth instanceof AnonymousAuthenticationToken){
             return new ResponseEntity<>("Please Log in",HttpStatus.UNAUTHORIZED);
        }
        
        Long id;
        try{
        String name = auth.getName();
        id = userService.findUserByUsername(name).get().getId();
        } catch(UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
        
        Order_DTO orderdto = userService.createOrderForUser(id, bookId);
      
       // String orderUrl = uriComponentsBuilder.path("/users/"+userId+"/orders/"+orderdto.getOrderId())
                     //       .toUriString();
      //  OrderResponse orderResponse = new OrderResponse(orderdto,orderUrl); 
      // HttpHeaders headers = new HttpHeaders();
      //  headers.setLocation(URI.create(orderUrl));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(orderdto);
        
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
