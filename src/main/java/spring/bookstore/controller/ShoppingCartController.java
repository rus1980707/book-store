package spring.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.bookstore.dto.AddToCartRequestDto;
import spring.bookstore.dto.ShoppingCartDto;
import spring.bookstore.dto.UpdateCartItemRequestDto;
import spring.bookstore.service.ShoppingCartService;

@Tag(name = "Shopping Cart", description = "Operations with user's shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @Operation(summary = "Get current user's shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shopping cart "
                    + "retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ShoppingCartDto getCart() {
        return cartService.getShoppingCart();
    }

    @Operation(summary = "Add book to shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book added to cart"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ShoppingCartDto addToCart(@RequestBody @Valid AddToCartRequestDto requestDto) {
        return cartService.addBookToCart(requestDto);
    }

    @Operation(summary = "Update quantity of cart item")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item updated"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return cartService.updateCartItem(cartItemId, requestDto);
    }

    @Operation(summary = "Remove item from shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart item removed"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/items/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
    }
}
