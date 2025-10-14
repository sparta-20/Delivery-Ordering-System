package com.delivery.domain.cart.entity;

import com.delivery.domain.store.entity.Store;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
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
    private CartStatusEnum status = CartStatusEnum.CART;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();

    @Builder
    public Cart(User user, Store store) {
        this.user = user;
        this.store = store;
    }

    public void addToCart(CartItem cartItem) {
        this.items.add(cartItem);
    }

    public void clearCart() {
        this.status = CartStatusEnum.CART_CANCEL;
    }
}
