package com.delivery.domain.user.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleEnumTest {

    @Test
    void testLessThan() {
        assertThat(UserRoleEnum.MASTER.isLowerThan(UserRoleEnum.MANAGER)).isFalse();
        assertThat(UserRoleEnum.MANAGER.isLowerThan(UserRoleEnum.MASTER)).isTrue();

        assertThat(UserRoleEnum.CUSTOMER.isLowerThan(UserRoleEnum.OWNER)).isTrue();
        assertThat(UserRoleEnum.OWNER.isLowerThan(UserRoleEnum.CUSTOMER)).isFalse();
    }
}
