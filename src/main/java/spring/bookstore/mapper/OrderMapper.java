package spring.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.bookstore.dto.OrderItemResponseDto;
import spring.bookstore.dto.OrderResponseDto;
import spring.bookstore.model.Order;
import spring.bookstore.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
