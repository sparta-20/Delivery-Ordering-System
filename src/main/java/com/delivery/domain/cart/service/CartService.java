package com.delivery.domain.cart.service;

import com.delivery.domain.cart.dto.CartReq;
import com.delivery.domain.cart.dto.CartRes;
import com.delivery.domain.cart.entity.Cart;

import java.util.UUID;

public interface CartService {
    Cart addToCart(Long userId, CartReq.AddCartItemDto dto);
    CartRes.CartListDto getCart(Long userId);
    void clearCart(Long userId);
    void updateCartItem(Long userId, UUID itemId, Integer quantity);

}
