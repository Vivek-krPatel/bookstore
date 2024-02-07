/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.service;

import com.example.bookstore.Exception.AddressNotFoundException;
import com.example.bookstore.models.Address;
import com.example.bookstore.repository.AddressRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author ACE
 */
@Service
@AllArgsConstructor
public class AddressService {
    
    private final AddressRepository addressRepository;
    
    
    public Optional<Address> getAddressForUser(Long id) throws AddressNotFoundException {
        Optional<Address> address = addressRepository.findByUserId(id);
        if(!address.isPresent()){
            throw new AddressNotFoundException(id);
        }
        
        return address;
    }
    
    
    public void saveAddressForUser(Address address){
        this.addressRepository.save(address);
    }
    
}
