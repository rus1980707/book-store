package spring.bookstore.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import spring.bookstore.model.Category;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ImportAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save() — should save category")
    void save_ok() {
        Category category = new Category();
        category.setName("Drama");
        category.setDescription("Books");

        Category saved = categoryRepository.save(category);

        assertNotNull(saved.getId());
        assertEquals("Drama", saved.getName());
    }

    @Test
    @DisplayName("findAllByIsDeletedFalse — should return only active categories")
    void findAllByIsDeletedFalse_ok() {
        Category active = new Category();
        active.setName("Sci-fi");
        active.setDeleted(false);
        categoryRepository.save(active);

        Category deleted = new Category();
        deleted.setName("Trash");
        deleted.setDeleted(true);
        categoryRepository.save(deleted);

        Page<Category> result = categoryRepository.findAllByIsDeletedFalse(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Sci-fi", result.getContent().get(0).getName());
    }

    @Test
    @DisplayName("delete() — should delete category")
    void delete_ok() {
        Category cat = new Category();
        cat.setName("Fantasy");
        Category saved = categoryRepository.save(cat);

        categoryRepository.deleteById(saved.getId());

        assertTrue(categoryRepository.findById(saved.getId()).isEmpty());
    }
}
