package com.delivery.cart.entity;

import com.delivery.common.entity.Timestamped;
import com.delivery.user.entity.User;
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
    private CartStatus status = CartStatus.CART;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();

    @Builder
    public Cart(User user) {
        this.user = user;
    }

    public void addToCart(CartItem cartItem) {
        this.items.add(cartItem);
    }

}
