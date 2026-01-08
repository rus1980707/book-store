package spring.bookstore.repository;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.bookstore.model.Category;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Given new Category, When save, Then it is persisted with generated ID")
    void shouldSaveCategory() {
        Category category = new Category();
        category.setName("Fiction");
        category.setDescription("Books");

        Category saved = categoryRepository.save(category);

        assertNotNull(saved.getId());
        assertEquals("Fiction", saved.getName());
    }

    @Test
    @DisplayName("Given saved Category, When findById, Then return Optional with Category")
    void shouldFindCategoryById() {
        Category category = new Category();
        category.setName("Science");
        categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findById(category.getId());

        assertTrue(found.isPresent());
    }
}
