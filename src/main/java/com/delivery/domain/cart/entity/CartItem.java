package com.delivery.domain.cart.entity;

import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "p_cart_item")
@Getter
public class CartItem extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartMenuId;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /*
    추후 수정
    private Menu menu;
     */

    @Column(nullable = false)
    private UUID menuId;

    @Column(nullable = false)
    private Integer price;

    @Builder
    public CartItem(Cart cart, UUID menuId, Integer quantity, Integer price) {
        this.cart = cart;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
