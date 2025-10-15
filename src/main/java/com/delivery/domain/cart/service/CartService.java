package com.delivery.domain.cart.service;

import com.delivery.domain.cart.dto.CartRequestDto;
import com.delivery.domain.cart.dto.CartResponseDto;
import com.delivery.domain.cart.entity.Cart;

import java.util.UUID;

public interface CartService {
    Cart addToCart(Long userId, CartRequestDto.AddCartItemDto dto);
    CartResponseDto.CartListDto getCart(Long userId);
    void clearCart(Long userId);
    void updateCartItem(Long userId, UUID itemId, Integer quantity);
}
