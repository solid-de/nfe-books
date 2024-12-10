package edu.cnam.nfe101.books.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cnam.nfe101.books.dto.AuthorDetails;
import edu.cnam.nfe101.books.dto.BookDetails;
import edu.cnam.nfe101.books.dto.BookSummary;
import edu.cnam.nfe101.books.dto.NewBook;
import edu.cnam.nfe101.books.dto.UpdateBook;
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
	

	public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}

	@GetMapping
	public ResponseEntity<List<BookSummary>> all() {

		List<BookSummary> books = bookRepository.findAll().stream() //
				.map(entity -> new BookSummary(entity.getBookId(), entity.getTitle())) //
				.toList();
		return ResponseEntity.ok()
				.body(books);
	}

	@PostMapping
	public ResponseEntity<BookDetails> newBook(@RequestBody NewBook newBook) {

		Author author = authorRepository.findById(newBook.authorId())
			.orElseThrow(() -> new AuthorNotFoundException(newBook.authorId()));
		Book newBookEntity = new Book();
		newBookEntity.setAuthor(author);
		newBookEntity.setTitle(newBook.title());
		Book createdBook = bookRepository.save(newBookEntity);
		AuthorDetails authorDetails = new AuthorDetails(author.getAuthorId(), author.getName(), author.getBio(), author.getCountry());
		BookDetails bookDetails = new BookDetails(createdBook.getBookId(), createdBook.getTitle(), authorDetails);

		return ResponseEntity //
				.ok() //
				.body(bookDetails);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDetails> one(@PathVariable Integer id) {

		Book book = bookRepository.findById(id) //
				.orElseThrow(() -> new BookNotFoundException(id));

		Author author = book.getAuthor();
		AuthorDetails authorDetails = new AuthorDetails(author.getAuthorId(), author.getName(), author.getBio(), author.getCountry());
		BookDetails bookDetails = new BookDetails(book.getBookId(), book.getTitle(), authorDetails);
		return ResponseEntity //
				.ok() //
				.body(bookDetails);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> replaceBook(@RequestBody UpdateBook newBook, @PathVariable Integer id) {

		Book updatedBook = bookRepository.findById(id) //
				.map(book -> {
					book.setTitle(newBook.title());

					return bookRepository.save(book);
				})
				.orElseThrow(() -> new BookNotFoundException(id));

		Author author = updatedBook.getAuthor();
		AuthorDetails authorDetails = new AuthorDetails(author.getAuthorId(), author.getName(), author.getBio(), author.getCountry());
		BookDetails bookDetails = new BookDetails(updatedBook.getBookId(), updatedBook.getTitle(), authorDetails);

		return ResponseEntity //
				.ok() //
				.body(bookDetails);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable Integer id) {

		bookRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

}
