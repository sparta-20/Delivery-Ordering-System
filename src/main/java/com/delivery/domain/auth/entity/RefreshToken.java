package com.delivery.domain.auth.entity;

import com.delivery.domain.user.entity.User;
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

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public RefreshToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public void updateToken(String newToken) {
        this.token = newToken;
    }
}