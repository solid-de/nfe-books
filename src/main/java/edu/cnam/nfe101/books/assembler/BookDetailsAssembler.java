package edu.cnam.nfe101.books.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import edu.cnam.nfe101.books.dto.AuthorDetails;
import edu.cnam.nfe101.books.dto.BookDetails;
import edu.cnam.nfe101.books.model.Book;
import edu.cnam.nfe101.books.rest.BookController;

@Component
public class BookDetailsAssembler implements RepresentationModelAssembler<Book, EntityModel<BookDetails>> {

    private final AuthorAssembler authorAssembler;
    
    public BookDetailsAssembler(AuthorAssembler authorAssembler) {
        this.authorAssembler = authorAssembler;
    }

    @Override
    public EntityModel<BookDetails> toModel(Book entity) {
        EntityModel<AuthorDetails> author = authorAssembler.toModel(entity.getAuthor());
        Link authorLink = author.getRequiredLink(IanaLinkRelations.SELF).withRel("author");
        BookDetails bookDetails = new BookDetails(entity.getBookId(), entity.getTitle(), author.getContent());
        return EntityModel.of(bookDetails, //
				linkTo(methodOn(BookController.class).one(entity.getBookId())).withSelfRel(),
				linkTo(methodOn(BookController.class).all()).withRel("books"),
                authorLink);
    }
    
}
