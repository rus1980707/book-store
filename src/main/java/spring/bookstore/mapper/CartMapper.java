package spring.bookstore.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.bookstore.dto.CartItemDto;
import spring.bookstore.dto.ShoppingCartDto;
import spring.bookstore.model.ShoppingCart;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", expression = "java(mapItems(shoppingCart))")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    default List<CartItemDto> mapItems(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream().map(item -> {
            CartItemDto dto = new CartItemDto();
            dto.setId(item.getId());
            dto.setBookId(item.getBook().getId());
            dto.setBookTitle(item.getBook().getTitle());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList());
    }
}
