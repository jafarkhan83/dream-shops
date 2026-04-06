package com.firstspringproject.dream_shops.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.firstspringproject.dream_shops.exceptions.CartNotFoundException;
import com.firstspringproject.dream_shops.model.Cart;
import com.firstspringproject.dream_shops.repository.CartRepository;
import com.firstspringproject.dream_shops.repository.CartitemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartitemRepository cartitemRepository;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
            .orElseThrow(() -> new CartNotFoundException("Cart not found!"));
        cart.updateTotalAmount();
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartitemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeNewCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart).getId();
    }
}
