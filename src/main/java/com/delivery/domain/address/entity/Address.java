package com.delivery.domain.address.entity;

import com.delivery.domain.user.entity.User;
import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_address")
public class Address extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private Boolean is_default = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Address(UUID addressId, String alias, String detailAddress, Boolean is_default, User user) {
        this.addressId = addressId;
        this.alias = alias;
        this.detailAddress = detailAddress;
        this.is_default = is_default;
        this.user = user;
    }
}
