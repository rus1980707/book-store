package spring.bookstore.repository;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.bookstore.model.Book;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("should save book successfully")
    void shouldSaveBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setIsbn("111-222-333");
        book.setPrice(BigDecimal.valueOf(100));

        Book saved = bookRepository.save(book);

        assertNotNull(saved.getId());
    }
}
