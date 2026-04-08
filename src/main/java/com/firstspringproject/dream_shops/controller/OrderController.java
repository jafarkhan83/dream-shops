package com.firstspringproject.dream_shops.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstspringproject.dream_shops.dto.OrderDto;
import com.firstspringproject.dream_shops.exceptions.OrderNotFoundException;
import com.firstspringproject.dream_shops.model.Order;
import com.firstspringproject.dream_shops.response.ApiResponse;
import com.firstspringproject.dream_shops.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/createOrder/{userId}")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("success!", orderService.convertToDto(order)));    
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }        
    }

    @GetMapping("/getOrderById/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("success!", orderService.convertToDto(order)));    
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getOrdersByUserId/{userId}")
    public ResponseEntity<ApiResponse> getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getUserOrders(userId);
            List<OrderDto> orderDtos = orders.stream().map(orderService::convertToDto).toList();
            return ResponseEntity.ok(new ApiResponse("success!", orderDtos));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
