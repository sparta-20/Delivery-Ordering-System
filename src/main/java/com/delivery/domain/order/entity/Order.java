package com.delivery.domain.order.entity;

import com.delivery.domain.store.entity.Store;
import com.delivery.global.common.entity.Timestamped;
import com.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

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
    private OrderStatusEnum status = OrderStatusEnum.PENDING;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String address;

    @Column
    private String canceledReason;

    @Column
    private String message;

    @Column
    private String deliveryMessage;

    @Column
    private Integer deliveryFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order")
    private List<OrderMenu> orderMenus = new ArrayList<>();

    public void changeStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public void rejectOrder(String reason) {
        this.status = OrderStatusEnum.REJECTED;
        this.canceledReason = reason;
    }

    public void cancel(String reason) {
        this.canceledReason = reason;
        this.status = OrderStatusEnum.CANCELED;
    }
}
