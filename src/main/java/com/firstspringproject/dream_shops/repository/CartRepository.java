package com.firstspringproject.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firstspringproject.dream_shops.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart getCartByUserId(Long userId);
    
}
