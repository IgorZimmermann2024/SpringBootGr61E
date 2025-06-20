package de.ait.javalessons.repository;

import de.ait.javalessons.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, String> {
}
