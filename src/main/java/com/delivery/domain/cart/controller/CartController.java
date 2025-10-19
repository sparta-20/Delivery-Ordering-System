package com.delivery.domain.cart.controller;

import com.delivery.domain.cart.dto.CartReq;
import com.delivery.domain.cart.dto.CartRes;
import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.service.CartService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders/cart")
@Tag(name = "Cart", description = "장바구니 API")
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "장바구니에 추가",
            description = "현재 로그인한 사용자의 장바구니에 메뉴를 추가합니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<ApiRes<UUID>> addToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartReq.AddCartItemDto dto) {
        User user = userDetails.getUser();
        Cart cart = cartService.addToCart(user.getUserId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiRes.success(cart.getCartId()));
    }

    @Operation(
            summary = "장바구니 조회",
            description = "현재 로그인한 사용자의 장바구니를 조회합니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<ApiRes<CartRes.CartListDto>> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        CartRes.CartListDto result = cartService.getCart(user.getUserId());
        return ResponseEntity.ok(ApiRes.success(result));
    }

    @Operation(
            summary = "장바구니 정보 조회 - admin",
            description = "관리자는 확인하고 싶은 유저의 장바구니를 조회할 수 있습니다."
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiRes<CartRes.CartListDto>> getCartByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @PathVariable Long userId) {
        CartRes.CartListDto result = cartService.getCart(userId);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    @Operation(
            summary = "장바구니 비우기",
            description = "현재 로그인한 사용자의 장바구니를 비웁니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        cartService.clearCart(user.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "장바구니 항목 개수 변경",
            description = "현재 장바구니에 담겨있는 메뉴 수량을 변경합니다."
    )
    @PreAuthorize("hasRole('CUSTOMER')")
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
