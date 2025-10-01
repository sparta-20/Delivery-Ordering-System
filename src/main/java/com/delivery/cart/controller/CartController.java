package com.delivery.cart.controller;

import com.delivery.cart.dto.CartRequestDto;
import com.delivery.cart.entity.Cart;
import com.delivery.cart.service.CartService;
import com.delivery.security.UserDetailsImpl;
import com.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartRequestDto.AddCartItemDto dto) {
        User user = userDetails.getUser();
        Cart cart = cartService.addToCart(user.getUserId(), dto);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/cart/" + cart.getCartId()))
                .build();
    }
}
