package com.firstspringproject.dream_shops.service.cart;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.firstspringproject.dream_shops.dto.CartDto;
import com.firstspringproject.dream_shops.exceptions.CartNotFoundException;
import com.firstspringproject.dream_shops.model.Cart;
import com.firstspringproject.dream_shops.model.User;
import com.firstspringproject.dream_shops.repository.CartRepository;
import com.firstspringproject.dream_shops.repository.CartitemRepository;
import com.firstspringproject.dream_shops.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartitemRepository cartitemRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;

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
    public Long initializeNewCart(Long userId) {
        Cart cart = new Cart();
        User user = userService.getUserById(userId);
        cart.setUser(user);
        return cartRepository.save(cart).getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        Cart cart = cartRepository.getCartByUserId(userId);
        return cart;
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        return modelMapper.map(cart, CartDto.class);
    }
}
