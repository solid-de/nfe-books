package edu.cnam.nfe101.books.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.cnam.nfe101.books.model.Book;


public interface BookRepository extends JpaRepository<Book, Integer> {
    
    List<Book> findByAuthorCountry(String country);

    @EntityGraph(attributePaths = {"author"})
    Optional<Book> findById(Integer id);

    List<Book> findByAuthorAuthorId(Integer authorId);
}
