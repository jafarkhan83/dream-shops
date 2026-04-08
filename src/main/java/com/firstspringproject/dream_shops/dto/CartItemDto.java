package com.firstspringproject.dream_shops.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
