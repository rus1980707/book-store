package spring.bookstore.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.bookstore.dto.OrderItemResponseDto;
import spring.bookstore.service.OrderItemService;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemResponseDto> getItems(
            @PathVariable Long orderId) {
        return orderItemService.getItemsByOrder(orderId);
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto getItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderItemService.getItemById(orderId, itemId);
    }
}
