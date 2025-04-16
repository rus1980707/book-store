package spring.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
