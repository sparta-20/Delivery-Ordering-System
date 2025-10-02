package com.delivery.ordering.user.service;

import com.delivery.exception.BusinessException;
import com.delivery.exception.ErrorCode;
import com.delivery.user.entity.User;
import com.delivery.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        long requesterId = 1L;
        long userId = 1L;

        User user = User.builder()
                .userId(userId)
                .build();
        user.cancelDeleted(user.getUserId());

        when(userRepository.findByUserIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        User result = userService.getUserIfAccessible(requesterId, userId);

        assertEquals(userId, result.getUserId());
    }

    @Test
    @DisplayName("다른 유저의 유저 정보를 조회할 수 없다.")
    void 다른_유저의_유저_정보를_조회할_수_없다() {
        long requesterId = 2L;
        long userId = 1L;

        User user = User.builder()
                .userId(userId)
                .build();
        user.cancelDeleted(user.getUserId());

        when(userRepository.findByUserIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.getUserIfAccessible(requesterId, userId)
        );

        assertEquals(ErrorCode.USER_ACCESS_DENIED, exception.getErrorCode());
    }
}
