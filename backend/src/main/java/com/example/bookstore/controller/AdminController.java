/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.controller;

import com.example.bookstore.DTO.Book_DTO;
import com.example.bookstore.models.Book;
import com.example.bookstore.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ACE
 */
@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@NoArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private BookService bookservice;
    
    //@PreAuthorize("Admin")
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody Book_DTO newbookdto){
        Book saveBook = mapper.map(newbookdto, Book.class);
        this.bookservice.create(saveBook);
        return new ResponseEntity<>(saveBook,HttpStatus.CREATED);
    }
    
    @DeleteMapping("/books/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id){
        this.bookservice.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
