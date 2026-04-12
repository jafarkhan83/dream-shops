package com.firstspringproject.dream_shops.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.firstspringproject.dream_shops.request.LoginRequest;
import com.firstspringproject.dream_shops.response.ApiResponse;
import com.firstspringproject.dream_shops.security.jwt.JwtUtils;
import com.firstspringproject.dream_shops.security.user.ShopUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor // This automatically injects your final variables
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        
        try {
            // 1. Wake up the Manager to verify the credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2. Set the verified user in the Spring Security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Print the actual JWT wristband
            String jwt = jwtUtils.generateTokenForUser(authentication);

            // 4. Extract user details to send a nice profile back to the frontend
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();

            // 5. Build the final JSON response
            Map<String, Object> data = new HashMap<>();
            data.put("id", userDetails.getId());
            data.put("email", userDetails.getEmail());
            data.put("token", jwt);

            return ResponseEntity.ok(new ApiResponse("success!", data));
    
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new ApiResponse("Invalid credentials", null));
        }

    }
}