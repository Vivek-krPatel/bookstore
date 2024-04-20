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
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CartService;
import com.example.bookstore.service.CartProductService;
import com.example.bookstore.service.UserDetailsService;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;



/**
 *
 * @author Vivek
 */
@RestController
@CrossOrigin()
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {
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
    public  ResponseEntity<?> all(Authentication auth, @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue="") String query, @RequestParam(defaultValue = "id") String sort){
        List<Book> books = this.bookService.findall(pageNo,query,sort);
        if(books == null){
            return ResponseEntity.notFound().build();
        }
        List<EntityModel<Book_DTO>> bookModels = books.stream().map( book ->
                EntityModel.of(mapper.map(book,Book_DTO.class),linkTo(methodOn(BookController.class).one(auth, book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all(auth,0,"",""))
                        .withRel("books")))
                    .collect(Collectors.toList());
        
        Link selfLink = linkTo(methodOn(BookController.class).all(auth,0,"","")).withSelfRel();
        
        CollectionModel<EntityModel<Book_DTO>> collectionModel = CollectionModel.of(bookModels,selfLink);
        return ResponseEntity.ok(collectionModel);  
    }
    
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<EntityModel<Book_DTO>> one(Authentication auth, @PathVariable Long id){
        try{
        Book book = this.bookService.findone(id);
        EntityModel<Book_DTO> bookModel = EntityModel.of(mapper.map(book,Book_DTO.class));
        Link selfLink = linkTo(methodOn(BookController.class).one(auth, id)).withSelfRel();
        //Link orderBookLink = linkTo(methodOn(BookController.class).createOrderForUser(auth, id)).withRel("place_order");
        Link booksLink = linkTo(methodOn(BookController.class).all(auth,0,"","")).withRel("books");
        
        bookModel.add(selfLink, booksLink);
        return ResponseEntity.ok(bookModel);
        } catch(BookNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchForBook(Authentication auth, @RequestParam(defaultValue="") String query, @RequestParam(defaultValue = "0") int pageNo){
        List<Book> books = this.bookService.search(query, pageNo);
        if(books == null){
            return ResponseEntity.notFound().build();
        }
        
        List<EntityModel<Book_DTO>> bookModels = books.stream().map( book ->
                EntityModel.of(mapper.map(book,Book_DTO.class),linkTo(methodOn(BookController.class).one(auth, book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all(auth,pageNo,"",""))
                        .withRel("books")))
                    .collect(Collectors.toList());
        
        Link selfLink = linkTo(methodOn(BookController.class).all(auth,0,"","")).withSelfRel();
        
        CollectionModel<EntityModel<Book_DTO>> collectionModel = CollectionModel.of(bookModels,selfLink);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/category")
    public ResponseEntity<?> searchForcategory(Authentication auth, @RequestParam(defaultValue="") Set<String> query, @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "id") String sort){
        List<Book> books = this.bookService.searchCategory(query, pageNo,sort);
        if(books == null){
            return ResponseEntity.notFound().build();
        }
        
        List<EntityModel<Book_DTO>> bookModels = books.stream().map( book ->
                EntityModel.of(mapper.map(book,Book_DTO.class),linkTo(methodOn(BookController.class).one(auth, book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all(auth,pageNo,"",""))
                        .withRel("books")))
                    .collect(Collectors.toList());
        
        Link selfLink = linkTo(methodOn(BookController.class).all(auth,0,"","")).withSelfRel();
        
        CollectionModel<EntityModel<Book_DTO>> collectionModel = CollectionModel.of(bookModels,selfLink);
        return ResponseEntity.ok(collectionModel);
    }
    
    
    
    @GetMapping("/filter")
    public ResponseEntity<?> getProducts(Authentication auth,@RequestParam(value="category",required=false) Set<String> category,@RequestParam(defaultValue = "asc",required=false) String sort, @RequestParam(defaultValue = "0") int pageNo){
        List<Book> books = this.bookService.filterAndSortBooksByCategory(category, sort,pageNo);
        if(books == null){
            return ResponseEntity.notFound().build();
        }
        
        //List<Book> books = this.bookService.filterAndSortBooksByCategory(category, sort,pageNo);
        List<EntityModel<Book_DTO>> bookModels = books.stream().map( book ->
                EntityModel.of(mapper.map(book,Book_DTO.class),linkTo(methodOn(BookController.class).one(auth, book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).all(auth,pageNo,"",""))
                        .withRel("books")))
                    .collect(Collectors.toList());
        
        Link selfLink = linkTo(methodOn(BookController.class).all(auth,0,"","")).withSelfRel();
        
        CollectionModel<EntityModel<Book_DTO>> collectionModel = CollectionModel.of(bookModels,selfLink);
        return ResponseEntity.ok(collectionModel);
    }
        

    
    @PreAuthorize("hasRole('USER')")
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ex.getMessage());
            //return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    @PreAuthorize("hasRole('USER')")
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
                   
                    Order_DTO orderDTO = this.userService.createOrderForUser(user.getId(), product);
                    return ResponseEntity.ok(orderDTO);
                    }catch (CartNotFoundException ex){
                        Cart cart = this.cartService.createCart(user);
                        CartProduct product = this.cartService.addItemToCart(request,cart.getId());
                        
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
   
    
    
   // private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Date parseDate(String date) {
            Date parsed = Date.valueOf(date);
            return parsed;
    }
        
    
    
}
