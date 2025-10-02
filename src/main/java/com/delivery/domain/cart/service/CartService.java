package com.delivery.domain.cart.service;

import com.delivery.domain.cart.dto.CartRequestDto;
import com.delivery.domain.cart.entity.Cart;

public interface CartService {
    Cart addToCart(Long userId, CartRequestDto dto);
}
