package com.delivery.cart.service;

import com.delivery.cart.entity.Cart;

import java.util.UUID;

public interface CartService {
    Cart addToCart(Long userId, UUID menuId, Integer quantity);
}
