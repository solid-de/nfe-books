package edu.cnam.nfe101.books.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cnam.nfe101.books.dto.AuthorDetails;
import edu.cnam.nfe101.books.dto.BookSummary;
import edu.cnam.nfe101.books.dto.NewAuthor;
import edu.cnam.nfe101.books.exceptions.AuthorNotFoundException;
import edu.cnam.nfe101.books.model.Author;
import edu.cnam.nfe101.books.repository.AuthorRepository;
import edu.cnam.nfe101.books.repository.BookRepository;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	public AuthorController(AuthorRepository authorRepository,
			BookRepository bookRepository) {
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
	}

	@GetMapping
	public ResponseEntity<List<AuthorDetails>> all() {

		List<AuthorDetails> authors = authorRepository.findAll().stream()
				.map(dbAuthor -> new AuthorDetails(dbAuthor.getAuthorId(), dbAuthor.getName(), dbAuthor.getBio(), dbAuthor.getCountry()))
				.toList();

		return ResponseEntity.ok().body(authors);
	}

	@PostMapping
	public ResponseEntity<?> newAuthor(@RequestBody NewAuthor newAuthor) {

		Author newAuthorEntity = new Author();
		newAuthorEntity.setName(newAuthor.name());
		newAuthorEntity.setBio(newAuthor.bio());
		newAuthorEntity.setCountry(newAuthor.country());

		Author dbCreated = authorRepository.save(newAuthorEntity);
		AuthorDetails result = new AuthorDetails(dbCreated.getAuthorId(), dbCreated.getName(), dbCreated.getBio(), dbCreated.getCountry());

		return ResponseEntity 
				.ok() 
				.body(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> one(@PathVariable Integer id) {

		Author author = authorRepository.findById(id) //
				.orElseThrow(() -> new AuthorNotFoundException(id));
		AuthorDetails result = new AuthorDetails(author.getAuthorId(), author.getName(), author.getBio(), author.getCountry());
		return ResponseEntity 
				.ok() 
				.body(result);
	}

	@GetMapping("/{id}/books")
	public ResponseEntity<List<BookSummary>> books(@PathVariable Integer id) {

		List<BookSummary> books = bookRepository.findByAuthorAuthorId(id).stream()
				.map(entity -> new BookSummary(entity.getBookId(), entity.getTitle()))
				.toList();
		return ResponseEntity
				.ok()
				.body(books);
	}

}
