package com.delivery.cart.service;

import com.delivery.cart.dto.CartRequestDto;
import com.delivery.cart.entity.Cart;

public interface CartService {
    Cart addToCart(Long userId, CartRequestDto dto);
}
