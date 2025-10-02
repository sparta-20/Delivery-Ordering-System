package com.delivery.domain.cart.repository;

import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.entity.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser_UserIdAndStatus(Long userId, CartStatus status);
}
