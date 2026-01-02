package spring.bookstore.service;

import java.util.List;
import spring.bookstore.dto.OrderItemResponseDto;

public interface OrderItemService {

    List<OrderItemResponseDto> getItemsByOrder(Long orderId);

    OrderItemResponseDto getItemById(Long orderId, Long itemId);
}
