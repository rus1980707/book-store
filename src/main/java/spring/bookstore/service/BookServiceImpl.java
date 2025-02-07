package spring.bookstore.service;

import java.util.List;
import org.springframework.stereotype.Service;
import spring.bookstore.model.Book;
import spring.bookstore.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {

        return bookRepository.findAll();
    }
}
