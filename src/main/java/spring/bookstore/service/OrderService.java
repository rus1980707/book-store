package spring.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.bookstore.dto.OrderItemResponseDto;
import spring.bookstore.dto.OrderRequestDto;
import spring.bookstore.dto.OrderResponseDto;
import spring.bookstore.dto.UpdateOrderStatusDto;

public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto requestDto);

    Page<OrderResponseDto> getUserOrders(Pageable pageable);

    OrderResponseDto updateStatus(Long orderId, UpdateOrderStatusDto dto);

    List<OrderItemResponseDto> getItemsByOrder(Long orderId);

    OrderItemResponseDto getItemById(Long orderId, Long itemId);
}
