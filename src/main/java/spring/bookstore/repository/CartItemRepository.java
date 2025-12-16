package spring.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.bookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
