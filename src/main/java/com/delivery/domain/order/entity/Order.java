package com.delivery.domain.order.entity;

import com.delivery.domain.store.entity.Store;
import com.delivery.global.common.entity.Timestamped;
import com.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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

    @Column
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order")
    @Builder.Default
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

    public boolean isOwnedBy(Long currentUserId) { return Objects.equals(this.user.getUserId(), currentUserId); }
    public boolean isDone() { return this.status == OrderStatusEnum.DONE; }
}
