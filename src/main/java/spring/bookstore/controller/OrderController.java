package spring.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.bookstore.dto.OrderRequestDto;
import spring.bookstore.dto.OrderResponseDto;
import spring.bookstore.dto.UpdateOrderStatusDto;
import spring.bookstore.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto placeOrder(
            @Valid @RequestBody OrderRequestDto requestDto) {
        return orderService.placeOrder(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<OrderResponseDto> getOrders(Pageable pageable) {
        return orderService.getUserOrders(pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponseDto updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusDto dto) {
        return orderService.updateStatus(id, dto);
    }
}
