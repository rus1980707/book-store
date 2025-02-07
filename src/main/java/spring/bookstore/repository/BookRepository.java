package spring.book_store.repository;

import java.util.List;
import spring.book_store.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
