package com.delivery.ordering.user.dto;

import com.delivery.user.entity.PublicStatus;
import com.delivery.user.entity.User;
import com.delivery.user.entity.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    @DisplayName("User를 이용해 생성할 수 있다.")
    void User를_이용해_생성할_수_있다() {
        // given
        User user = User.builder()
                .userId(1L)
                .nickname("tester")
                .email("test@example.com")
                .role(UserRoleEnum.CUSTOMER)
                .publicStatus(PublicStatus.PUBLIC)
                .build();

        // when
        UserResponse response = UserResponse.from(user);

        // then
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getNickname()).isEqualTo("tester");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getRole()).isEqualTo(UserRoleEnum.CUSTOMER);
        assertThat(response.getIsPublic()).isEqualTo(PublicStatus.PUBLIC);
    }
}