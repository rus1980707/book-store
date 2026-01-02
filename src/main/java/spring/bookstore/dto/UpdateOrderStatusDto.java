package spring.bookstore.dto;

import lombok.Getter;
import lombok.Setter;
import spring.bookstore.model.OrderStatus;

@Getter
@Setter
public class UpdateOrderStatusDto {
    private OrderStatus status;
}
