package spring.bookstore.repository;

import java.util.List;
import spring.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
