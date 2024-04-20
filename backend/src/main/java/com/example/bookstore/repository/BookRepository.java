/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.bookstore.repository;

import com.example.bookstore.models.Book;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vivek
 */
@Repository
public interface BookRepository extends JpaRepository<Book,Long>,PagingAndSortingRepository<Book,Long>  {
    
    //public Page<Book> findAll(String query, Pageable page);
    public Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);
    
    @Query(value="Select * from books where lower(category) like lower(%?1%)",
            countQuery="select count(*) from books",
            nativeQuery=true)
    public Page<Book> findByCategory(String category, Pageable page);
    
    
    @Query(value="Select * from books where category in ?1",
            countQuery="select count(*) from books",
            nativeQuery=true)
    public Page<Book> findByCategoryIn(Set<String> categories, Pageable page);
    
    
    
    @Query(value="Select * from books where rating >= ?1 order by rating asc",
            countQuery="select count(*) from books",
            nativeQuery=true)
    public Page<Book> findByRatingGreaterThanOrEqual(float rating, Pageable page);
    
    @Query(value="Select * from books where rating <= ?1 order by rating desc",
            countQuery="select count(*) from books",
            nativeQuery=true)
    public Page<Book> findByRatingLessThanOrEqual(float rating, Pageable page);
    
  /*
    
    @Query("Select b from books b " + "where (b.category IN :category or :category is null) "+ "ORDER BY "
            + "CASE WHEN :sort = 'low' THEN rating ASC END, "
            + "CASE WHEN :sort = 'high' THEN rating DESC END")
    public List<Book> filterBooks(@Param("category") Set<String> category, @Param("sort") String sort);
    
*/
   
    //Page<Book> findByCategoryIn(Set<String> categories, Pageable page);
 
}
