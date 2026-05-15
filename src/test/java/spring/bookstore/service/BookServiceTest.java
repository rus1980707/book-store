package spring.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spring.bookstore.AbstractIntegrationTest;
import spring.bookstore.dto.BookDto;
import spring.bookstore.dto.CreateBookRequestDto;
import spring.bookstore.exception.EntityNotFoundException;
import spring.bookstore.model.Book;
import spring.bookstore.model.Category;
import spring.bookstore.repository.BookRepository;
import spring.bookstore.repository.CategoryRepository;

class BookServiceTest extends AbstractIntegrationTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("getBookById returns DTO if found")
    void getBookById_ok() {
        Book saved = bookRepository.save(book("Dune", "Frank Herbert", "1234567890"));

        BookDto actual = bookService.getBookById(saved.getId());

        assertEquals(saved.getId(), actual.getId());
        assertEquals("Dune", actual.getTitle());
    }

    @Test
    @DisplayName("getBookById throws EntityNotFoundException when not found")
    void getBookById_notFound() {
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(99L));
    }

    @Test
    @DisplayName("createBook saves and returns DTO")
    void createBook_ok() {
        Category category = categoryRepository.save(category("Programming"));
        CreateBookRequestDto request = bookRequest(
                "Clean Code",
                "Robert Martin",
                "9780132350884",
                List.of(category.getId())
        );

        BookDto actual = bookService.createBook(request);

        assertNotNull(actual.getId());
        assertEquals("Clean Code", actual.getTitle());
        assertFalse(bookRepository.findById(actual.getId()).isEmpty());
    }

    @Test
    @DisplayName("updateBook updates existing book")
    void updateBook_ok() {
        Book saved = bookRepository.save(book("Old Title", "Someone", "ABC"));
        Category category = categoryRepository.save(category("Updated category"));
        CreateBookRequestDto request = bookRequest(
                "New Title",
                "Someone",
                "ABC",
                List.of(category.getId())
        );

        BookDto actual = bookService.updateBook(saved.getId(), request);

        assertEquals(saved.getId(), actual.getId());
        assertEquals("New Title", actual.getTitle());
    }

    @Test
    @DisplayName("deleteBook deletes existing book")
    void delete_ok() {
        Book saved = bookRepository.save(book("Refactoring", "Fowler", "333"));

        bookService.deleteById(saved.getId());

        assertFalse(bookRepository.existsById(saved.getId()));
    }

    private Book book(String title, String author, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.ONE);
        return book;
    }

    private Category category(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    private CreateBookRequestDto bookRequest(String title, String author, String isbn,
                                             List<Long> categoryIds) {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle(title);
        request.setAuthor(author);
        request.setIsbn(isbn);
        request.setPrice(BigDecimal.TEN);
        request.setCategoryIds(categoryIds);
        return request;
    }
}
