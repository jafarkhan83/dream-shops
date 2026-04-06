package com.firstspringproject.dream_shops.service.cart;

import com.firstspringproject.dream_shops.model.Cartitem;

public interface ICartitemService {
    void addItemToCart (Long cartId, Long productId, int quantity);
    void removeItemFromCart (Long cartId, Long productId);
    void updateItemQuantity (Long cartId, Long productId, int quantity);
    Cartitem getCartitem(Long cartId, Long productId);
}
