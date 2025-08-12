package com.bookstorage.repository;

import com.bookstorage.entity.Book;
import com.bookstorage.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByOwner(User owner);
    
    Page<Book> findByOwner(User owner, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.owner = :owner AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Book> findByOwnerAndSearchTerm(@Param("owner") User owner, 
                                       @Param("searchTerm") String searchTerm, 
                                       Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:ownerId IS NULL OR b.owner.id = :ownerId)")
    Page<Book> findByFilters(@Param("title") String title,
                            @Param("author") String author,
                            @Param("ownerId") Long ownerId,
                            Pageable pageable);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.owner = :owner")
    long countByOwner(@Param("owner") User owner);
    
    @Query("SELECT b FROM Book b WHERE b.owner.id = :ownerId")
    List<Book> findByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT b FROM Book b WHERE b.owner.id = :ownerId ORDER BY b.createdAt DESC")
    Page<Book> findByOwnerIdOrderByCreatedAtDesc(@Param("ownerId") Long ownerId, Pageable pageable);
}
