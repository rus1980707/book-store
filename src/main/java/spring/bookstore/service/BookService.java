package spring.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.bookstore.dto.BookDto;
import spring.bookstore.dto.BookDtoWithoutCategoryIds;
import spring.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    List<BookDto> getAll();

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequestDto bookDto);

    BookDto updateBook(Long id, CreateBookRequestDto bookDto);

    void deleteById(long id);

    Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable);

}
