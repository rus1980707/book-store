package spring.bookstore.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategories_Id(Long categoryId);

    Page<Book> findAllByCategories_Id(Long categoryId, Pageable pageable);

}
