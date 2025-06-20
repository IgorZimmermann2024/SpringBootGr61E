package de.ait.javalessons.controller;

import de.ait.javalessons.model.Book;
import de.ait.javalessons.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/books")
public class RestApiBookController {


    private final BookRepository bookRepository;

    public RestApiBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        /**this.bookRepository.saveAll(List.of(
                new Book("1", "Clean Code", "Robert C. Martin", 2008),
                new Book("2", "1984", "George Orwell", 1949),
                new Book("3", "Effective Java", "Joshua Bloch", 2018),
                new Book("4", "The Great Gatsby", "F. Scott Fitzgerald", 1925),
                new Book("5", "Refactoring", "Martin Fowler", 1999),
                new Book("6", "To Kill a Mockingbird", "Harper Lee", 1960),
                new Book("7", "The Pragmatic Programmer", "Andrew Hunt, David Thomas", 1999)));*/
    }


    @GetMapping
    Iterable<Book> getBooks() {
        log.info("Getting all books");
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> getBookById(@PathVariable String id) {
        Optional <Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            log.info("Book with id {} found", id);
            return ResponseEntity.status(HttpStatus.OK).body(book.get());
        }
        log.warn("Book with id {} not found", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity<Book> postBook(@Valid @RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        log.info("Book with id {} added", book.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    ResponseEntity<Book> putBook(@PathVariable String id, @RequestBody Book book) {
        if(bookRepository.existsById(id)){
            Book bookInDatabase = bookRepository.findById(id).get();
            book.setId(bookInDatabase.getId());
            book.setAuthor(bookInDatabase.getAuthor());
            book.setPublishYear(bookInDatabase.getPublishYear());
            book.setTitle(bookInDatabase.getTitle());
            bookRepository.save(book);
            log.info("Book with id {} updated", id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
            return postBook(book);
    }

    @DeleteMapping("/{id}")
    void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(id);
        log.info("Book with id {} deleted", id);
    }

}
