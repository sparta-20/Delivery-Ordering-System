package com.delivery.domain.user.entity;

import com.delivery.global.common.entity.Timestamped;
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
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.CUSTOMER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus = PublicStatus.PUBLIC;

    public User(String nickname, String email, String password, String phoneNumber) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public boolean isCustomer() {
        return UserRoleEnum.CUSTOMER.equals(this.role);
    }

    public boolean isMaster() {
        return UserRoleEnum.MASTER.equals(this.role);
    }

    public boolean isOwner() {
        return UserRoleEnum.OWNER.equals(this.role);
    }

    public boolean isManager() {
        return UserRoleEnum.MANAGER.equals(this.role);
    }

    public void update(String nickname, String email, PublicStatus publicStatus, String phoneNumber) {
        this.nickname = nickname;
        this.email = email;
        this.publicStatus = publicStatus;
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String encodePassword) {
        this.password = encodePassword;
    }
}
