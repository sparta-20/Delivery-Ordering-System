package com.delivery.domain.cart.repository;

import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartAndMenuId(Cart cart, UUID menuId);
}
