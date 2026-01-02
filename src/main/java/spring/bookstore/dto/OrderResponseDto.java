package spring.bookstore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import spring.bookstore.model.OrderStatus;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private OrderStatus status;
}
