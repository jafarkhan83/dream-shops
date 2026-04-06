package com.firstspringproject.dream_shops.service.cart;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.firstspringproject.dream_shops.exceptions.CartItemNotFoundException;
import com.firstspringproject.dream_shops.model.Cart;
import com.firstspringproject.dream_shops.model.Cartitem;
import com.firstspringproject.dream_shops.model.Product;
import com.firstspringproject.dream_shops.repository.CartRepository;
import com.firstspringproject.dream_shops.repository.CartitemRepository;
import com.firstspringproject.dream_shops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartitemService implements ICartitemService {
    private final CartitemRepository cartitemRepository;
    private final IProductService productService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        Cartitem cartItem = cart.getCartItems().stream()
            .filter(item -> Objects.equals(item.getProduct().getId(), productId))
            .findFirst().orElse(null);
        if (cartItem != null) cartItem.setQuantity(quantity);
        else {
            cartItem = new Cartitem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setQuantity(quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartitemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        Cartitem cartItem = getCartitem(cartId, productId);
        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Cartitem cartItem = getCartitem(cartId, productId);
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice();
        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

    @Override
    public Cartitem getCartitem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        Cartitem cartItem = cart.getCartItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst().orElseThrow(() -> new CartItemNotFoundException("cart item not found!"));
        return cartItem;
    }

}