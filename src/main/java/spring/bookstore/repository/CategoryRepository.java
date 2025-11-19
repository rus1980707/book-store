package spring.bookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.bookstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDeletedFalse();
}
