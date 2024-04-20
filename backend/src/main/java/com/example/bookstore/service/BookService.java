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
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vivek
 */
@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    
    
    public List<Book> findall(int pageNo,String query, String sort){
        Page books;
       
            Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(sort)); 
            books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query,query,pageable);
       
        if (books.isEmpty()){
            return null; 
        }
        return books.getContent();
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

    public List<Book> search(String query, int pageNo) {
        Pageable pageRequest = PageRequest.of(pageNo,10) ;
        Page<Book> books = this.bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageRequest);
        if(books.isEmpty()){
            return null;
        }
        return books.getContent();
    }
    
    public List<Book> searchCategory(Set<String> query, int pageNo, String sort) {
        Page books;
        if(sort.equalsIgnoreCase("")){
            Pageable pageable = PageRequest.of(pageNo, 10); 
            books = bookRepository.findByCategoryIn(query, pageable);
        }else{
            Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(sort)); 
            books = bookRepository.findByCategoryIn(query, pageable);
        }
      
        if(books.isEmpty()){
            return null;
        }
        return books.getContent();
    }
    
    public List<Book> searchRatingGreaterOrEqual(float rating, int pageNo) {
        Pageable pageRequest = PageRequest.of(pageNo,10) ;
        Page<Book> books = this.bookRepository.findByRatingGreaterThanOrEqual(rating, pageRequest);
        if(books.isEmpty()){
            return null;
        }
        return books.getContent();
    }
    
    public List<Book> searchRatingLessOrEqual(float rating, int pageNo) {
        Pageable pageRequest = PageRequest.of(pageNo,10) ;
        Page<Book> books = this.bookRepository.findByRatingLessThanOrEqual(rating, pageRequest);
        if(books.isEmpty()){
            return null;
        }
        return books.getContent();
    }
    
    public List<Book> filterAndSortBooksByCategory(Set<String> categories, String sort,Integer pageNo) {
        Pageable pageRequest = PageRequest.of(pageNo,10,   Sort.by("rating").ascending());
        //Sort sortByRating = Sort.by(Sort.Direction.ASC, "rating");

        if (sort.equalsIgnoreCase("desc")) {
            //sortByRating = Sort.by(Sort.Direction.DESC, "rating");
            pageRequest = PageRequest.of(pageNo,10,   Sort.by("rating").descending());
        }
        
        Page<Book> books = this.bookRepository.findByCategoryIn(categories,pageRequest);
        if(books.isEmpty()){
            return null;
        }
        return books.getContent();
    }

}
