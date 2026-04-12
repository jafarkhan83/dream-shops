package com.firstspringproject.dream_shops.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.firstspringproject.dream_shops.exceptions.CartItemNotFoundException;
import com.firstspringproject.dream_shops.exceptions.UserNotExistsException;
import com.firstspringproject.dream_shops.model.User;
import com.firstspringproject.dream_shops.response.ApiResponse;
import com.firstspringproject.dream_shops.service.cart.ICartService;
import com.firstspringproject.dream_shops.service.cart.ICartitemService;
import com.firstspringproject.dream_shops.service.user.IUserService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartitems")
public class CartItemController {
    private final ICartitemService cartitemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required=false) Long cartId, @RequestParam Long productId, @RequestParam int quantity, @RequestParam Long userId) {
        try {
            User user = userService.getAuthenticatedUser();
            if (cartId == null) cartId = cartService.initializeNewCart(user.getId());
            cartitemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("success!", null));    
        } catch (CartItemNotFoundException | UserNotExistsException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
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
