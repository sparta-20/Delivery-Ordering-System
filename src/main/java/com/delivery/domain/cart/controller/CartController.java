package com.delivery.domain.cart.controller;

import com.delivery.domain.cart.dto.CartRequestDto;
import com.delivery.domain.cart.dto.CartResponseDto;
import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.service.CartService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

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

    @PatchMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        cartService.clearCart(user.getUserId());
        
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("cartItemId") UUID cartItemId,
            @RequestParam Integer quantity) {
        User user = userDetails.getUser();
        cartService.updateCartItem(user.getUserId(), cartItemId, quantity);
        return ResponseEntity.noContent().build();
    }
}
