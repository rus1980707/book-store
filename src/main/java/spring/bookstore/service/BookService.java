package spring.bookstore.service;

import java.util.List;
import spring.bookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
