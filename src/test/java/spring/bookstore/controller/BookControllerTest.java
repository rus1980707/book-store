package spring.bookstore.controller;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import spring.bookstore.dto.BookDto;
import spring.bookstore.security.JwtAuthenticationFilter;
import spring.bookstore.security.JwtUtil;
import spring.bookstore.service.BookService;
import spring.bookstore.service.CategoryService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    @DisplayName("GET /books should return list of books")
    void shouldReturnAllBooks() throws Exception {
        BookDto dto = new BookDto();
        dto.setId(1L);
        dto.setTitle("Clean Code");

        when(bookService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    @DisplayName("DELETE /books/{id} — deletes book (ADMIN role)")
    @WithMockUser(roles = "ADMIN")
    void deleteBook_ok() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }

}

