package com.delivery.ordering.domain.user.service;

import com.delivery.domain.user.dto.UpdateUserRequest;
import com.delivery.domain.user.entity.PublicStatus;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @MockitoBean private UserRepository userRepository;
    @Autowired private UserService userService;

    @Test
    @DisplayName("나의 유저정보를 조회할 수 있다.")
    void 나의_유저정보를_조회할_수_있다() {
        long userId = 1L;

        User user = User.builder()
                .userId(userId)
                .build();
        user.cancelDeleted(user.getUserId());

        when(userRepository.findByUserIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(userId);

        assertEquals(userId, result.getUserId());
    }

    @Test
    @DisplayName("나의 유저정보를 갱신할 수 있다.")
    void 나의_유저정보를_갱신할_수_있다() {
        long userId = 1L;
        String oldEmail = "email@email.com";
        String oldNickName = "nick";
        PublicStatus oldPublicStatus = PublicStatus.PUBLIC;

        User user = User.builder()
                .userId(userId)
                .email(oldEmail)
                .nickname(oldNickName)
                .publicStatus(oldPublicStatus)
                .build();
        user.cancelDeleted(user.getUserId());
        UpdateUserRequest req = new UpdateUserRequest("newNick", "newEmail@enamil.com", PublicStatus.PRIVATE);

        when(userRepository.findByUserIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        User result = userService.updateUser(userId, req);

        assertEquals(userId, result.getUserId());
        assertNotEquals(oldNickName, result.getNickname());
        assertNotEquals(oldEmail, result.getEmail());
        assertNotEquals(oldPublicStatus, result.getPublicStatus());
    }
}
