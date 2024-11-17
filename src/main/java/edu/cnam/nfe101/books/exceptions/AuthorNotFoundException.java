package edu.cnam.nfe101.books.exceptions;

public class AuthorNotFoundException extends NotFoundException {

    public AuthorNotFoundException(Integer id) {
        super("Can not find author with ID: " + id);
    }
     
}
