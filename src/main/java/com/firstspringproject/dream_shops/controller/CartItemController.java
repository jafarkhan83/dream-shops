package com.firstspringproject.dream_shops.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.firstspringproject.dream_shops.exceptions.CartItemNotFoundException;
import com.firstspringproject.dream_shops.response.ApiResponse;
import com.firstspringproject.dream_shops.service.cart.ICartService;
import com.firstspringproject.dream_shops.service.cart.ICartitemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartitems")
public class CartItemController {
    private final ICartitemService cartitemService;
    private final ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required=false) Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        try {
            if (cartId == null) cartId = cartService.initializeNewCart();
            cartitemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("success!", null));    
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/remove/{cartId}/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart (@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            cartitemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("success!", null));   
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{cartId}/{productId}")
    public ResponseEntity<ApiResponse> updateItemQuantity (@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        try {
            cartitemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("success!", null));    
        } catch (CartItemNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
