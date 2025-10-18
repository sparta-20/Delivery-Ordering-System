package com.delivery.domain.cart.repository;

import com.delivery.domain.cart.entity.Cart;
import com.delivery.domain.cart.entity.CartStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser_UserIdAndStatus(Long userId, CartStatusEnum statusEnum);
    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.items ci " +
            "LEFT JOIN FETCH ci.menu " +
            "LEFT JOIN FETCH c.store " +
            "WHERE c.user.userId = :userId AND c.status = :status")
    Optional<Cart> findByUser_UserIdAndStatusWithItems(
            @Param("userId") Long userId,
            @Param("status") CartStatusEnum status);
}
