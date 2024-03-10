/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.controller;

import com.example.bookstore.Exception.AddressNotFoundException;
import com.example.bookstore.Exception.BadCredentialsException;
import com.example.bookstore.Exception.BookNotFoundException;
import com.example.bookstore.Exception.CartNotFoundException;
import com.example.bookstore.Exception.UserAlreadyExistsException;
import com.example.bookstore.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author ACE
 */
@ControllerAdvice
@ResponseBody
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookNotFound(BookNotFoundException ex) {
        return ex.getMessage();
        
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserAlreadyExist(UserAlreadyExistsException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserAlreadyExist(UserNotFoundException ex){
        return ex.getMessage();
    }
    
    @ExceptionHandler(AddressNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAddressNotFound(UserNotFoundException ex){
        return ex.getMessage();
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadCredentialsException(BadCredentialsException ex){
        return ex.getMessage();
    }
    
    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCartNotFoundException(CartNotFoundException ex){
        return ex.getMessage();
    }
    
}
