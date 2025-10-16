package com.delivery.domain.cart.controller;

import com.delivery.domain.cart.dto.CartReq;
import com.delivery.domain.cart.dto.CartRes;
import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.service.CartService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiRes<UUID>> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartReq.AddCartItemDto dto) {
        User user = userDetails.getUser();
        Cart cart = cartService.addToCart(user.getUserId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiRes.success(cart.getCartId()));
    }

    @GetMapping
    public ResponseEntity<ApiRes<CartRes.CartListDto>> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        CartRes.CartListDto result = cartService.getCart(user.getUserId());
        return ResponseEntity.ok(ApiRes.success(result));
    }

    @PatchMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        cartService.clearCart(user.getUserId());
        return ResponseEntity.noContent().build();
    }
        
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Void> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID itemId,
            @RequestBody CartReq.UpdateCartItemDto dto) {
        User user = userDetails.getUser();
        cartService.updateCartItem(user.getUserId(), itemId, dto.getQuantity());
        return ResponseEntity.noContent().build();
    }
}
