package com.delivery.cart.service;

import com.delivery.cart.dto.CartRequestDto;
import com.delivery.cart.dto.CartResponseDto;
import com.delivery.cart.entity.Cart;

public interface CartService {
    Cart addToCart(Long userId, CartRequestDto.AddCartItemDto dto);
    CartResponseDto.CartListDto getCart(Long userId);
}
