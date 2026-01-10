package spring.bookstore.repository;

import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import spring.bookstore.model.Book;
import spring.bookstore.model.Category;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save() — should save book")
    void save_ok() {
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(200));

        Book saved = bookRepository.save(book);

        assertNotNull(saved.getId());
        assertEquals("Clean Code", saved.getTitle());
    }

    @Test
    @DisplayName("findAllByCategories_Id pageable — should return page")
    void findAllByCategoryId_pageable_ok() {
        Category category = new Category();
        category.setName("Tech");
        category = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Martin");
        book.setIsbn("222");
        book.setPrice(BigDecimal.ONE);
        book.setCategories(Set.of(category));
        bookRepository.save(book);

        Page<Book> page = bookRepository.findAllByCategories_Id(
                category.getId(),
                PageRequest.of(0, 5)
        );

        assertEquals(1, page.getTotalElements());
        assertEquals("Clean Code", page.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("delete() — should delete book")
    void delete_ok() {
        Book book = new Book();
        book.setTitle("Refactoring");
        book.setAuthor("Fowler");
        book.setIsbn("333");
        book.setPrice(BigDecimal.ONE);

        Book saved = bookRepository.save(book);
        bookRepository.deleteById(saved.getId());

        assertTrue(bookRepository.findById(saved.getId()).isEmpty());
    }
}
