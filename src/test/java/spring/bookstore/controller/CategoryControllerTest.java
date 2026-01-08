package spring.bookstore.controller;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import spring.bookstore.dto.CategoryDto;
import spring.bookstore.security.JwtAuthenticationFilter;
import spring.bookstore.security.JwtUtil;
import spring.bookstore.service.BookService;
import spring.bookstore.service.CategoryService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /categories -> should return paginated list of categories")
    void shouldReturnCategories() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Drama");
        dto.setDescription("Books");

        when(categoryService.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Drama"));
    }
    @Test
    @DisplayName("DELETE /categories/{id} - delete")
    void delete_ok() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(1L);
    }
    @Test
    @DisplayName("GET /categories/{id}/books - paginated books")
    void getBooks_ok() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookService.findAllByCategoryId(eq(5L), any()))
                .thenReturn(Page.empty(pageable));

        mockMvc.perform(get("/categories/5/books?page=0&size=10"))
                .andExpect(status().isOk());

        verify(bookService).findAllByCategoryId(eq(5L), any());
    }
}

