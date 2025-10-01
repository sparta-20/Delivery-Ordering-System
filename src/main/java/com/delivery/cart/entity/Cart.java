package com.delivery.cart.entity;

import com.delivery.common.entity.Timestamped;
import com.delivery.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "p_cart")
@Getter
public class Cart extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.CART;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* Menu 연결 필요
    private Menu menu;
     */

    // 추후 수정될 항목
    @Column(nullable = false)
    private UUID menuId;

    @Builder
    public Cart(Integer quantity, User user, UUID menuId) {
        this.quantity = quantity;
        this.user = user;
        this.menuId = menuId;
    }
}
