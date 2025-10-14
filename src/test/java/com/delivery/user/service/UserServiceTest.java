package com.delivery.user.service;

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
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
}
