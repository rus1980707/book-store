package spring.bookstore.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import spring.bookstore.model.Category;
import spring.bookstore.repository.CategoryRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("POST /categories — creates category (ADMIN role)")
    @WithMockUser(roles = "ADMIN")
    void createCategory_ok() throws Exception {
        String json = """
            {
              "name": "Fantasy",
              "description": "Books with magic"
            }
        """;

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @DisplayName("POST /categories — 400 validation error")
    @WithMockUser(roles = "ADMIN")
    void createCategory_validationError() throws Exception {
        String json = """
            {
              "name": ""
            }
        """;

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /categories — returns categories (USER)")
    @WithMockUser(roles = "USER")
    void getAllCategories_ok() throws Exception {
        Category c = new Category();
        c.setName("Fantasy");
        categoryRepository.save(c);

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Fantasy"));
    }

    @Test
    @DisplayName("GET /categories/{id} — returns category (USER)")
    @WithMockUser(roles = "USER")
    void getCategoryById_ok() throws Exception {
        Category c = new Category();
        c.setName("Fantasy");
        c = categoryRepository.save(c);

        mockMvc.perform(get("/categories/" + c.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @DisplayName("GET /categories/{id} — 404 not found")
    @WithMockUser(roles = "USER")
    void getCategoryById_notFound() throws Exception {
        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /categories/{id} — updates category (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ok() throws Exception {
        Category c = new Category();
        c.setName("Fantasy");
        c = categoryRepository.save(c);

        String json = """
            {
              "name": "Sci-Fi",
              "description": "Updated"
            }
        """;

        mockMvc.perform(put("/categories/" + c.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sci-Fi"));
    }

    @Test
    @DisplayName("DELETE /categories/{id} — deletes category (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_ok() throws Exception {
        Category c = new Category();
        c.setName("Fantasy");
        c = categoryRepository.save(c);

        mockMvc.perform(delete("/categories/" + c.getId()))
                .andExpect(status().isNoContent());
    }
}

