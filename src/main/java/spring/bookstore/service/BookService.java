package spring.book_store.service;

import java.util.List;
import spring.book_store.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
