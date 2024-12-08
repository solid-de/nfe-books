package edu.cnam.nfe101.books.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cnam.nfe101.books.assembler.BookAssembler;
import edu.cnam.nfe101.books.assembler.BookDetailsAssembler;
import edu.cnam.nfe101.books.dto.BookDetails;
import edu.cnam.nfe101.books.dto.BookSummary;
import edu.cnam.nfe101.books.dto.CustomItemsListModel;
import edu.cnam.nfe101.books.dto.NewBook;
import edu.cnam.nfe101.books.exceptions.AuthorNotFoundException;
import edu.cnam.nfe101.books.exceptions.BookNotFoundException;
import edu.cnam.nfe101.books.model.Author;
import edu.cnam.nfe101.books.model.Book;
import edu.cnam.nfe101.books.repository.AuthorRepository;
import edu.cnam.nfe101.books.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {
	private final BookRepository bookRepository;

	private final AuthorRepository authorRepository;
	
	private final BookAssembler bookAssembler;
	private final BookDetailsAssembler bookDetailsAssembler;

	public BookController(BookRepository bookRepository, AuthorRepository authorRepository, BookAssembler bookAssembler,
			BookDetailsAssembler bookDetailsAssembler) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.bookAssembler = bookAssembler;
		this.bookDetailsAssembler = bookDetailsAssembler;
	}

	@GetMapping
	public CustomItemsListModel<BookSummary> all() {

		List<EntityModel<BookSummary>> books = bookRepository.findAll().stream() //
				.map(entity -> bookAssembler.toModel(entity)) //
				.collect(Collectors.toList());

		CustomItemsListModel<BookSummary> booksModel = new CustomItemsListModel<>(books)
				.add(linkTo(methodOn(BookController.class).all()).withSelfRel());
		return booksModel;
	}

	@PostMapping
	public ResponseEntity<?> newBook(@RequestBody NewBook newBook) {

		Author author = authorRepository.findById(newBook.authorId())
			.orElseThrow(() -> new AuthorNotFoundException(newBook.authorId()));

		Book newBookEntity = new Book();
		newBookEntity.setAuthor(author);
		newBookEntity.setTitle(newBook.title());
		
		EntityModel<BookDetails> entityModel = bookDetailsAssembler.toModel(bookRepository.save(newBookEntity));

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@GetMapping("/{id}")
	public EntityModel<BookDetails> one(@PathVariable Integer id) {

		Book book = bookRepository.findById(id) //
				.orElseThrow(() -> new BookNotFoundException(id));

		return bookDetailsAssembler.toModel(book);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> replaceBook(@RequestBody Book newBook, @PathVariable Integer id) {

		Book updatedBook = bookRepository.findById(id) //
				.map(book -> {
					book.setTitle(newBook.getTitle());

					return bookRepository.save(book);
				})
				.orElseGet(() -> {
					return bookRepository.save(newBook);
				});

		EntityModel<BookDetails> entityModel = bookDetailsAssembler.toModel(updatedBook);

		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable Integer id) {

		bookRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
