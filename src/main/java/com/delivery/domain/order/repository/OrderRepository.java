package com.delivery.domain.order.repository;

import com.delivery.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser_UserIdAndDeletedAtIsNull(Long userId);
    List<Order> findByStore_Owner_UserId(Long ownerUserId);
    Optional<Order> findByOrderId(UUID orderId);
    Optional<Order> findByOrderIdAndDeletedAtIsNull(UUID orderId);
}
