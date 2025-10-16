package com.delivery.domain.order.repository;

import com.delivery.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByUser_UserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
    Page<Order> findByStore_Owner_UserId(Long ownerUserId, Pageable pageable);
    Optional<Order> findByOrderId(UUID orderId);
    Optional<Order> findByOrderIdAndDeletedAtIsNull(UUID orderId);
}
