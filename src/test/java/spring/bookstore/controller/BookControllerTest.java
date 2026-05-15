package spring.bookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import spring.bookstore.AbstractIntegrationTest;
import spring.bookstore.model.Book;
import spring.bookstore.model.Category;
import spring.bookstore.model.Role;
import spring.bookstore.repository.BookRepository;
import spring.bookstore.repository.CategoryRepository;

@AutoConfigureMockMvc
class BookControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("GET /books returns persisted books")
    void getAll_ok() throws Exception {
        Book book = saveBook("Clean Code", "Robert Martin", "9780132350884");

        mockMvc.perform(get("/books")
                        .headers(authHeaders(Role.RoleName.ROLE_USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(book.getId()))
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Martin"))
                .andExpect(jsonPath("$[0].isbn").value("9780132350884"));
    }

    @Test
    @DisplayName("GET /books/{id} returns single book")
    void getById_ok() throws Exception {
        Book book = saveBook("DDDD", "Vernon", "1111111111");

        mockMvc.perform(get("/books/" + book.getId())
                        .headers(authHeaders(Role.RoleName.ROLE_USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("DDDD"));
    }

    @Test
    @DisplayName("POST /books creates book")
    void create_ok() throws Exception {
        Category category = saveCategory("Refactoring category");
        HttpHeaders headers = authHeaders(Role.RoleName.ROLE_ADMIN);
        String json = """
            {
              "title":"Refactoring",
              "author":"Fowler",
              "isbn":"9780201485677",
              "description":"Refactoring book",
              "price":55,
              "categoryIds": [%d]
            }
            """.formatted(category.getId());

        mockMvc.perform(post("/books")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Refactoring"))
                .andExpect(jsonPath("$.author").value("Fowler"));
    }

    @Test
    @DisplayName("PUT /books/{id} updates book")
    void update_ok() throws Exception {
        Category category = saveCategory("Update category");
        Book book = saveBook("Old Title", "Someone", "ABC");
        HttpHeaders headers = authHeaders(Role.RoleName.ROLE_ADMIN);
        String json = """
            {
              "title":"New Title",
              "author":"Someone",
              "isbn":"ABC",
              "price":10,
              "categoryIds": [%d]
            }
            """.formatted(category.getId());

        mockMvc.perform(put("/books/" + book.getId())
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    @DisplayName("DELETE /books/{id} deletes book")
    void delete_ok() throws Exception {
        Book book = saveBook("To Delete", "AA", "BB");

        mockMvc.perform(delete("/books/" + book.getId())
                        .headers(authHeaders(Role.RoleName.ROLE_ADMIN)))
                .andExpect(status().isNoContent());
    }

    private Book saveBook(String title, String author, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.ONE);
        book.setDescription("Description");
        return bookRepository.save(book);
    }

    private Category saveCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
}
