package com.firstspringproject.dream_shops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.firstspringproject.dream_shops.dto.OrderDto;
import com.firstspringproject.dream_shops.enums.OrderStatus;
import com.firstspringproject.dream_shops.exceptions.CartNotFoundException;
import com.firstspringproject.dream_shops.exceptions.OrderNotFoundException;
import com.firstspringproject.dream_shops.model.Cart;
import com.firstspringproject.dream_shops.model.Order;
import com.firstspringproject.dream_shops.model.OrderItem;
import com.firstspringproject.dream_shops.model.Product;
import com.firstspringproject.dream_shops.repository.OrderRepository;
import com.firstspringproject.dream_shops.repository.ProductRepository;
import com.firstspringproject.dream_shops.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) throw new CartNotFoundException("This user has no cart!");
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(totalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(cartItem.getQuantity(), cartItem.getUnitPrice(), product, order);
        }).toList();
    }

    private BigDecimal totalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
            .map(item -> item.getPrice()
            .multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrder(Long orderID) {
        Order order = orderRepository.findById(orderID)
            .orElseThrow(() -> new OrderNotFoundException("No order exists by this id!"));
        return order;
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
