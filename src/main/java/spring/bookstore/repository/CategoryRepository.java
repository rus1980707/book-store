package spring.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.bookstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAllByIsDeletedFalse(Pageable pageable);
}
