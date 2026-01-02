package spring.bookstore.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.bookstore.dto.OrderItemResponseDto;
import spring.bookstore.exception.EntityNotFoundException;
import spring.bookstore.mapper.OrderMapper;
import spring.bookstore.model.Order;
import spring.bookstore.model.OrderItem;
import spring.bookstore.model.User;
import spring.bookstore.repository.OrderItemRepository;
import spring.bookstore.repository.OrderRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final AuthenticationService authenticationService;

    @Override
    public List<OrderItemResponseDto> getItemsByOrder(Long orderId) {
        User user = authenticationService.getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found. id=" + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("Order not found");
        }

        return order.getOrderItems().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getItemById(Long orderId, Long itemId) {
        User user = authenticationService.getCurrentUser();

        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order item not found. id=" + itemId));

        if (!item.getOrder().getId().equals(orderId)
                || !item.getOrder().getUser().getId().equals(user.getId())) {
            throw new EntityNotFoundException("Order item not found");
        }

        return orderMapper.toDto(item);
    }
}
