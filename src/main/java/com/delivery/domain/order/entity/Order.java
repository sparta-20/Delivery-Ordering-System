package com.delivery.domain.order.entity;

import com.delivery.global.common.entity.Timestamped;
import com.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "p_order")
@Getter
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Store 추가 필요

    @OneToMany(mappedBy = "order")
    private List<OrderMenu> orderMenus = new ArrayList<>();
}
