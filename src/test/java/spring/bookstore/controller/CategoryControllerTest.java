package spring.bookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import spring.bookstore.AbstractIntegrationTest;
import spring.bookstore.model.Category;
import spring.bookstore.model.Role;
import spring.bookstore.repository.CategoryRepository;

@AutoConfigureMockMvc
class CategoryControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("POST /categories creates category")
    void createCategory_ok() throws Exception {
        String json = """
            {
              "name": "Fantasy",
              "description": "Books with magic"
            }
            """;

        mockMvc.perform(post("/categories")
                        .headers(authHeaders(Role.RoleName.ROLE_ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @DisplayName("POST /categories returns validation error")
    void createCategory_validationError() throws Exception {
        String json = """
            {
              "name": ""
            }
            """;

        mockMvc.perform(post("/categories")
                        .headers(authHeaders(Role.RoleName.ROLE_ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /categories returns categories")
    void getAllCategories_ok() throws Exception {
        saveCategory("Fantasy");

        mockMvc.perform(get("/categories")
                        .headers(authHeaders(Role.RoleName.ROLE_USER))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Fantasy"));
    }

    @Test
    @DisplayName("GET /categories/{id} returns category")
    void getCategoryById_ok() throws Exception {
        Category category = saveCategory("Fantasy");

        mockMvc.perform(get("/categories/" + category.getId())
                        .headers(authHeaders(Role.RoleName.ROLE_USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @DisplayName("GET /categories/{id} returns 404")
    void getCategoryById_notFound() throws Exception {
        mockMvc.perform(get("/categories/999")
                        .headers(authHeaders(Role.RoleName.ROLE_USER)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /categories/{id} updates category")
    void updateCategory_ok() throws Exception {
        Category category = saveCategory("Fantasy");
        String json = """
            {
              "name": "Sci-Fi",
              "description": "Updated"
            }
            """;

        mockMvc.perform(put("/categories/" + category.getId())
                        .headers(authHeaders(Role.RoleName.ROLE_ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sci-Fi"));
    }

    @Test
    @DisplayName("DELETE /categories/{id} deletes category")
    void deleteCategory_ok() throws Exception {
        Category category = saveCategory("Fantasy");

        mockMvc.perform(delete("/categories/" + category.getId())
                        .headers(authHeaders(Role.RoleName.ROLE_ADMIN)))
                .andExpect(status().isNoContent());
    }

    private Category saveCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
}
