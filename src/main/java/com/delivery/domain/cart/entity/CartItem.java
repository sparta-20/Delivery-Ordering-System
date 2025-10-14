package com.delivery.domain.cart.entity;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Builder
    public CartItem(Cart cart, Menu menu, Integer quantity) {
        this.cart = cart;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
