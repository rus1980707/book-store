package spring.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spring.bookstore.AbstractIntegrationTest;
import spring.bookstore.dto.CategoryDto;
import spring.bookstore.dto.CategoryRequestDto;
import spring.bookstore.model.Category;
import spring.bookstore.repository.CategoryRepository;

class CategoryServiceTest extends AbstractIntegrationTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("getById returns DTO")
    void getById_ok() {
        Category saved = categoryRepository.save(category("Drama"));

        CategoryDto actual = categoryService.getById(saved.getId());

        assertEquals(saved.getId(), actual.getId());
        assertEquals("Drama", actual.getName());
    }

    @Test
    @DisplayName("deleteById deletes category when exists")
    void deleteById_ok() {
        Category saved = categoryRepository.save(category("Fantasy"));

        categoryService.deleteById(saved.getId());

        assertFalse(categoryRepository.existsById(saved.getId()));
    }

    @Test
    @DisplayName("createCategory saves and returns DTO")
    void create_ok() {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("Fantasy");
        request.setDescription("Books");

        CategoryDto actual = categoryService.save(request);

        assertNotNull(actual.getId());
        assertEquals("Fantasy", actual.getName());
    }

    private Category category(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
