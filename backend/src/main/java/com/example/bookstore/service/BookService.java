/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.service;

import com.example.bookstore.Exception.BookNotFoundException;
import com.example.bookstore.models.Book;
import com.example.bookstore.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACE
 */
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    
    public List<Book> findall(){
        List<Book> books = bookRepository.findAll(); 
        return books;
    }

    public void create(Book newbook) {
         this.bookRepository.save(newbook);
    }

    public Book findone(Long id) {
        Optional<Book> optionalbook = bookRepository.findById(id);
        if(!optionalbook.isPresent()){
            throw new BookNotFoundException(id);
        }
        return optionalbook.get();
    }

    public void delete(Long id) {
        if(this.bookRepository.existsById(id)){
            this.bookRepository.deleteById(id);
        }
    }
}
