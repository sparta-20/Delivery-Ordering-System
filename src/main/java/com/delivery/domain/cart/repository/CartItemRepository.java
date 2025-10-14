package com.delivery.domain.cart.repository;

import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.entity.CartItem;
import com.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartAndMenu(Cart cart, Menu menu);
    Optional<CartItem> findByCartMenuIdAndCart_User_UserId(UUID cartMenuID, Long userId);
}
