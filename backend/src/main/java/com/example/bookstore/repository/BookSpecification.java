/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.repository;

import com.example.bookstore.models.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Vivek
 */
public class BookSpecification {

    public static Specification<Book> filterBooks(String category, Integer rating, String author, Integer year) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (category != null) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("category"), category));
            }
            if (rating != null) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("rating"), rating));
            }
            if (author != null) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("author"), author));
            }
            if (year != null) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("year"), year));
            }
            
            return criteriaBuilder.and((jakarta.persistence.criteria.Predicate[]) predicates.toArray(new Predicate[0]));
        };
    }
}
