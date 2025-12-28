package spring.bookstore.service;

import spring.bookstore.dto.AddToCartRequestDto;
import spring.bookstore.dto.ShoppingCartDto;
import spring.bookstore.dto.UpdateCartItemRequestDto;
import spring.bookstore.model.User;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart();

    ShoppingCartDto addBookToCart(AddToCartRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto);

    void removeCartItem(Long cartItemId);

    void createCartForUser(User user);
}
