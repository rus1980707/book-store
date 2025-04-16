package spring.bookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.bookstore.dto.BookDto;
import spring.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    Page<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequestDto bookDto);

    BookDto updateBook(Long id, CreateBookRequestDto bookDto);

    void deleteById(long id);
}
