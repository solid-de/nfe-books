package edu.cnam.nfe101.books.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import edu.cnam.nfe101.books.dto.AuthorDetails;
import edu.cnam.nfe101.books.model.Author;
import edu.cnam.nfe101.books.rest.AuthorController;

@Component
public class AuthorAssembler implements RepresentationModelAssembler<Author, EntityModel<AuthorDetails>> {

    @Override
    public EntityModel<AuthorDetails> toModel(Author entity) {
        AuthorDetails details = new AuthorDetails(entity.getAuthorId(), entity.getName(), entity.getBio(), entity.getCountry());
        return EntityModel.of(details, //
				linkTo(methodOn(AuthorController.class).one(entity.getAuthorId())).withSelfRel(),
				linkTo(methodOn(AuthorController.class).books(entity.getAuthorId())).withRel("books"),
                linkTo(methodOn(AuthorController.class).all()).withRel("authors")
                );
    }
    
}
