package spring.bookstore.controller;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import spring.bookstore.model.Book;
import spring.bookstore.repository.BookRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ImportAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("GET /books — returns persisted books (USER)")
    @WithMockUser(roles = "USER")
    void getAll_ok() throws Exception {
        Book b = new Book();
        b.setTitle("Clean Code");
        b.setAuthor("Robert Martin");
        b.setIsbn("9780132350884");
        b.setPrice(BigDecimal.valueOf(30));
        b.setDescription("Classic");

        bookRepository.save(b);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].author").value("Robert Martin"))
                .andExpect(jsonPath("$[0].isbn").value("9780132350884"));
    }


    @Test
    @DisplayName("GET /books/{id} — returns single book (USER)")
    @WithMockUser(roles = "USER")
    void getById_ok() throws Exception {
        Book b = new Book();
        b.setTitle("DDDD");
        b.setAuthor("Vernon");
        b.setIsbn("1111111111");
        b.setPrice(BigDecimal.TEN);
        b.setDescription("ddd");
        Book saved = bookRepository.save(b);

        mockMvc.perform(get("/books/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("DDDD"));
    }

    @Test
    @DisplayName("POST /books — creates book (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void create_ok() throws Exception {
        String json = """
            {
              "title":"Refactoring",
              "author":"Fowler",
              "isbn":"9780201485677",
              "description":"Refactoring book",
              "price":55,
              "categoryIds": [1]
            }
            """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Refactoring"))
                .andExpect(jsonPath("$.author").value("Fowler"));
    }

    @Test
    @DisplayName("PUT /books/{id} — updates book (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void update_ok() throws Exception {
        Book b = new Book();
        b.setTitle("Old Title");
        b.setAuthor("Someone");
        b.setIsbn("ABC");
        b.setPrice(BigDecimal.ONE);
        b.setDescription("d");
        Book saved = bookRepository.save(b);

        String json = """
            {
              "title":"New Title",
              "author":"Someone",
              "isbn":"ABC",
              "price":10,
               "categoryIds": [1]
            }
            """;

        mockMvc.perform(put("/books/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    @DisplayName("DELETE /books/{id} — soft deletes (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void delete_ok() throws Exception {
        Book b = new Book();
        b.setTitle("To Delete");
        b.setAuthor("AA");
        b.setIsbn("BB");
        b.setPrice(BigDecimal.ONE);
        b.setDescription("d");
        Book saved = bookRepository.save(b);

        mockMvc.perform(delete("/books/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}
