package spring.book_store.service;

import org.springframework.stereotype.Service;
import spring.book_store.model.Book;
import spring.book_store.repository.BookRepository;
import java.util.List;

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
