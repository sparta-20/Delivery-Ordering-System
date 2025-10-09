package com.delivery.user.entity;

import com.delivery.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Table(name = "p_user")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.CUSTOMER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus = PublicStatus.PUBLIC;

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
