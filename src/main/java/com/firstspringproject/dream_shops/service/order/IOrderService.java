package com.firstspringproject.dream_shops.service.order;

import java.util.List;

import com.firstspringproject.dream_shops.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderID);
    List<Order> getUserOrders(Long userId);
}
