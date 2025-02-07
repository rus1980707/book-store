package spring.book_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.book_store.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
