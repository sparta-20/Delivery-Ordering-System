package com.delivery.order.repository;

import com.delivery.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.orderMenus m " +
            "WHERE o.user.userId = :userId " +
            "AND m.status = com.delivery.order.entity.OrderMenuStatus.ORDER")
    List<Order> findByUser_UserId(Long userId);
}
