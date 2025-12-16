package spring.bookstore.service;

import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.bookstore.dto.AddToCartRequestDto;
import spring.bookstore.dto.ShoppingCartDto;
import spring.bookstore.dto.UpdateCartItemRequestDto;
import spring.bookstore.exception.EntityNotFoundException;
import spring.bookstore.mapper.CartMapper;
import spring.bookstore.model.Book;
import spring.bookstore.model.CartItem;
import spring.bookstore.model.ShoppingCart;
import spring.bookstore.model.User;
import spring.bookstore.repository.BookRepository;
import spring.bookstore.repository.CartItemRepository;
import spring.bookstore.repository.ShoppingCartRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final CartMapper cartMapper;
    private final AuthenticationService authenticationService;

    @Override
    public ShoppingCartDto getShoppingCart() {
        User user = authenticationService.getCurrentUser();

        ShoppingCart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));
        cart.getCartItems().forEach(cartItem ->
                cartItem.getBook().getTitle());
        return cartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto addBookToCart(AddToCartRequestDto requestDto) {
        User user = authenticationService.getCurrentUser();

        ShoppingCart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> Objects.equals(
                        item.getBook().getId(),book.getId()))
                .findFirst().orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity());
            requestDto.getQuantity();
        } else {
            CartItem newItem = new CartItem();
            newItem.setShoppingCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(requestDto.getQuantity());
            cart.getCartItems().add(newItem);
        }
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        item.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(item);

        ShoppingCart cart = item.getShoppingCart();
        cart.getCartItems().forEach(cartItem -> cartItem.getBook().getTitle());
        return cartMapper.toDto(cart);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        ShoppingCart cart = item.getShoppingCart();
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);

    }
}
