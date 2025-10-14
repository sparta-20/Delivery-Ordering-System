package com.delivery.domain.order.entity;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "p_order_menu")
@Getter
public class OrderMenu extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderMenuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderMenuStatusEnum status = OrderMenuStatusEnum.ORDER;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

}
