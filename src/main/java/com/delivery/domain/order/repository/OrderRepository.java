package com.delivery.domain.order.repository;

import com.delivery.domain.order.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser_UserId(Long userId);
    List<Order> findByStore_Owner_UserId(Long ownerUserId);
    Optional<Order> findByOrderId(UUID orderId);
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.userId = :userId AND o.store.storeId = :storeId AND o.createdAt >= :from")
    Integer countByUserAndStoreSince(
            @Param("userId") Long userId,
            @Param("storeId") UUID storeId,
            @Param("from") LocalDateTime from);
}
