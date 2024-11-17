package edu.cnam.nfe101.books.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import edu.cnam.nfe101.books.dto.BookSummary;
import edu.cnam.nfe101.books.model.Book;
import edu.cnam.nfe101.books.rest.BookController;

@Component
public class BookAssembler implements RepresentationModelAssembler<Book, EntityModel<BookSummary>> {

    @Override
    public EntityModel<BookSummary> toModel(Book entity) {
        BookSummary summary = new BookSummary(entity.getBookId(), entity.getTitle());
        return EntityModel.of(summary, //
				linkTo(methodOn(BookController.class).one(entity.getBookId())).withSelfRel(),
				linkTo(methodOn(BookController.class).all()).withRel("books"));
    }
    
}
