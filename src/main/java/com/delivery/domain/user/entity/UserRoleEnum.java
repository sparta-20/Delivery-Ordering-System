package com.delivery.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    CUSTOMER("ROLE_CUSTOMER",3),
    OWNER("ROLE_OWNER",2),
    MANAGER("ROLE_MANAGER", 1),
    MASTER("ROLE_MASTER", 0);

    private final String authority;
    private final Integer rank;

    public boolean isLowerThan(UserRoleEnum role) {
        return this.rank >= role.rank;
    }

    public boolean equalsRank(UserRoleEnum role) {
        return this.rank.equals(role.rank);
    }
}