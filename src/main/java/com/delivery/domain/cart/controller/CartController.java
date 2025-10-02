package com.delivery.domain.cart.controller;


import com.delivery.domain.cart.dto.CartRequestDto;
import com.delivery.domain.cart.dto.CartResponseDto;
import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.service.CartService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<CartResponseDto.CartListDto> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        CartResponseDto.CartListDto result = cartService.getCart(user.getUserId());
        return ResponseEntity.ok(result);
    }
}
