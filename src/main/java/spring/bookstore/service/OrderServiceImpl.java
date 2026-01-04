package spring.bookstore.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.bookstore.dto.OrderItemResponseDto;
import spring.bookstore.dto.OrderRequestDto;
import spring.bookstore.dto.OrderResponseDto;
import spring.bookstore.dto.UpdateOrderStatusDto;
import spring.bookstore.exception.EntityNotFoundException;
import spring.bookstore.mapper.OrderMapper;
import spring.bookstore.model.CartItem;
import spring.bookstore.model.Order;
import spring.bookstore.model.OrderItem;
import spring.bookstore.model.OrderStatus;
import spring.bookstore.model.ShoppingCart;
import spring.bookstore.model.User;
import spring.bookstore.repository.OrderItemRepository;
import spring.bookstore.repository.OrderRepository;
import spring.bookstore.repository.ShoppingCartRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final AuthenticationService authenticationService;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {
        User user = authenticationService.getCurrentUser();

        ShoppingCart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id: " + user.getId()));

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());

            order.getOrderItems().add(orderItem);

            total = total.add(
                    cartItem.getBook().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setTotal(total);
        orderRepository.save(order);

        cart.getCartItems().clear();

        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderResponseDto> getUserOrders(Pageable pageable) {
        User user = authenticationService.getCurrentUser();

        return orderRepository.findAllByUserId(user.getId(), pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto updateStatus(Long orderId, UpdateOrderStatusDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found. id=" + orderId));

        order.setStatus(dto.getStatus());
        return orderMapper.toDto(order);
    }

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
