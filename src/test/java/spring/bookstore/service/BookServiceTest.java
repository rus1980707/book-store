package spring.bookstore.service;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.bookstore.dto.BookDto;
import spring.bookstore.dto.CreateBookRequestDto;
import spring.bookstore.exception.EntityNotFoundException;
import spring.bookstore.mapper.BookMapper;
import spring.bookstore.model.Book;
import spring.bookstore.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("getBookById — returns DTO if found")
    void getBookById_ok() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Dune");

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Dune");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.getBookById(1L);

        assertEquals(expected, actual);
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("getBookById — throws EntityNotFoundException when not found")
    void getBookById_notFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(99L));
        verify(bookRepository).findById(99L);
    }

    @Test
    @DisplayName("createBook — saves and returns DTO")
    void createBook_ok() {
        CreateBookRequestDto req = new CreateBookRequestDto();
        req.setTitle("Clean Code");

        Book saved = new Book();
        saved.setId(1L);
        saved.setTitle("Clean Code");

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Clean Code");

        when(bookMapper.toEntity(req)).thenReturn(saved);
        when(bookRepository.save(saved)).thenReturn(saved);
        when(bookMapper.toDto(saved)).thenReturn(expected);

        BookDto actual = bookService.createBook(req);

        assertEquals(expected, actual);
        verify(bookMapper).toEntity(req);
        verify(bookRepository).save(saved);
        verify(bookMapper).toDto(saved);
    }

    @Test
    @DisplayName("updateBook — updates when exists")
    void updateBook_ok() {
        CreateBookRequestDto req = new CreateBookRequestDto();
        req.setTitle("Dune");

        Book existing = new Book();
        existing.setId(1L);

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Dune");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookMapper.toDto(existing)).thenReturn(expected);

        BookDto actual = bookService.updateBook(1L, req);

        verify(bookMapper).updateBookDto(req, existing);
        verify(bookRepository).save(existing);
        assertEquals("Dune", actual.getTitle());
    }

    @Test
    @DisplayName("deleteBook — calls repository delete")
    void delete_ok() {
        bookService.deleteById(5L);
        verify(bookRepository).deleteById(5L);
    }
}
