package edu.cnam.nfe101.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.cnam.nfe101.books.model.Author;


public interface AuthorRepository extends JpaRepository<Author, Integer> {
    
}
