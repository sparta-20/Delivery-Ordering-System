package com.delivery.cart.repository;

import com.delivery.cart.entity.Cart;
import com.delivery.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartAndMenuId(Cart cart, UUID menuId);
    Optional<CartItem> findByCartMenuIdAndCart_User_UserId(UUID cartMenuID, Long userId);
}
