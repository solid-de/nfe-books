package edu.cnam.nfe101.books.exceptions;

public class BookNotFoundException extends NotFoundException {

    public BookNotFoundException(Integer id) {
        super("Can not find book with ID: " + id);
    }
     
}
