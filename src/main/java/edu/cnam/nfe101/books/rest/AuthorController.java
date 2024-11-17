package edu.cnam.nfe101.books.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cnam.nfe101.books.assembler.AuthorAssembler;
import edu.cnam.nfe101.books.assembler.BookAssembler;
import edu.cnam.nfe101.books.dto.AuthorDetails;
import edu.cnam.nfe101.books.dto.BookSummary;
import edu.cnam.nfe101.books.dto.CustomItemsListModel;
import edu.cnam.nfe101.books.dto.NewAuthor;
import edu.cnam.nfe101.books.exceptions.AuthorNotFoundException;
import edu.cnam.nfe101.books.model.Author;
import edu.cnam.nfe101.books.repository.AuthorRepository;
import edu.cnam.nfe101.books.repository.BookRepository;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	private final AuthorRepository authorRepository;
	private final AuthorAssembler authorAssembler;
	private final BookRepository bookRepository;
	private final BookAssembler bookAssembler;

	public AuthorController(AuthorRepository authorRepository, AuthorAssembler authorAssembler,
			BookRepository bookRepository, BookAssembler bookAssembler) {
		this.authorRepository = authorRepository;
		this.authorAssembler = authorAssembler;
		this.bookRepository = bookRepository;
		this.bookAssembler = bookAssembler;
	}

	@GetMapping
	public CustomItemsListModel<AuthorDetails> all() {

		List<EntityModel<AuthorDetails>> authors = authorRepository.findAll().stream() //
				.map(authorAssembler::toModel) //
				.collect(Collectors.toList());

		CustomItemsListModel<AuthorDetails> authorsModel = new CustomItemsListModel<>(authors)
				.add(linkTo(methodOn(BookController.class).all()).withSelfRel());

		return authorsModel;
	}

	@PostMapping
	public ResponseEntity<?> newAuthor(@RequestBody NewAuthor newAuthor) {

		Author newAuthorEntity = new Author();
		newAuthorEntity.setName(newAuthor.name());
		newAuthorEntity.setBio(newAuthor.bio());
		newAuthorEntity.setCountry(newAuthor.country());

		EntityModel<AuthorDetails> entityModel = authorAssembler.toModel(authorRepository.save(newAuthorEntity));

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@GetMapping("/{id}")
	public EntityModel<AuthorDetails> one(@PathVariable Integer id) {

		Author author = authorRepository.findById(id) //
				.orElseThrow(() -> new AuthorNotFoundException(id));

		return authorAssembler.toModel(author);
	}

	@GetMapping("/{id}/books")
	public CustomItemsListModel<BookSummary> books(@PathVariable Integer id) {

		List<EntityModel<BookSummary>> books = bookRepository.findByAuthorAuthorId(id).stream()
				.map(bookAssembler::toModel)
				.collect(Collectors.toList());
		CustomItemsListModel<BookSummary> booksModel = new CustomItemsListModel<>(books)
				.add(linkTo(methodOn(AuthorController.class).one(id)).withSelfRel());
		return booksModel;
	}

}
