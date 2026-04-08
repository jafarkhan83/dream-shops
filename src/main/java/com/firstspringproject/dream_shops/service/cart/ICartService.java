package com.firstspringproject.dream_shops.service.cart;

import java.math.BigDecimal;

import com.firstspringproject.dream_shops.dto.CartDto;
import com.firstspringproject.dream_shops.model.Cart;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart (Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeNewCart(Long userId);
    Cart getCartByUserId(Long userId);
    CartDto convertToDto(Cart cart);
}
