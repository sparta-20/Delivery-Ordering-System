package com.delivery.domain.auth.entity;

import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="p_refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    public RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public void updateToken(String newToken) {
        this.token = newToken;
    }
}