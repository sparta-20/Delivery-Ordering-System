package com.delivery.domain.menu.service;

import com.delivery.domain.user.dto.UpdateUserPasswordReq;
import com.delivery.domain.user.dto.UpdateUserReq;
import com.delivery.domain.user.dto.UserRes;
import com.delivery.domain.user.entity.PublicStatus;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @MockitoBean private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저정보를 조회할 수 있다.")
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
    @DisplayName("유저정보를 갱신할 수 있다.")
    void 유저정보를_갱신할_수_있다() {
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
        UpdateUserReq req = new UpdateUserReq("newNick", "newEmail@enamil.com", PublicStatus.PRIVATE, "01012341234");
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        UserRes result = userService.updateUser(userId, req);

        assertEquals(userId, result.getUserId());
        assertNotEquals(oldNickName, result.getNickname());
        assertNotEquals(oldEmail, result.getEmail());
        assertNotEquals(oldPublicStatus, result.getIsPublic());
    }

    @Test
    @DisplayName("유저정보를 갱신할 때 존재하는 닉네임이면 예외를 발생한다.")
    void 유저정보를_갱신할_때_존재하는_닉네임이면_예외를_발생한다() {
        long userId = 1L;
        String oldNickName = "nick";
        String changeNickname = "existNick";
        User user = User.builder()
                .userId(userId)
                .nickname(oldNickName)
                .build();
        UpdateUserReq req = new UpdateUserReq(changeNickname, "newEmail@enamil.com", PublicStatus.PRIVATE, "01012341234");
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByNickname(changeNickname)).thenReturn(true);

        BusinessException businessException = assertThrows(BusinessException.class, () -> {
            userService.updateUser(userId, req);
        });

        assertEquals(ErrorCode.DUPLICATE_NICKNAME, businessException.getErrorCode());
    }

    @Test
    @DisplayName("유저정보를 갱신할 때 존재하는 이메일이면 예외를 발생한다.")
    void 유저정보를_갱신할_때_존재하는_이메일이면_예외를_발생한다() {
        long userId = 1L;
        String oldEmail = "email@email.com";
        String changeEmail = "exist@email.com";
        User user = User.builder().userId(userId).email(oldEmail).build();
        UpdateUserReq req = new UpdateUserReq("nick", changeEmail, PublicStatus.PRIVATE, "01012341234");
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(changeEmail)).thenReturn(true);

        BusinessException businessException = assertThrows(BusinessException.class, () -> {
            userService.updateUser(userId, req);
        });

        assertEquals(ErrorCode.DUPLICATE_EMAIL, businessException.getErrorCode());
    }

    @Test
    @DisplayName("유저 비밀번호를 변경할 수 있다.")
    void 유저_비밀번호를_변경할_수_있다() {
        long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(currentPassword))
                .build();
        UpdateUserPasswordReq req = new UpdateUserPasswordReq(currentPassword, newPassword, newPassword);
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));

        UserRes userRes = userService.updateUserPassword(userId, req);

        assertNotNull(userRes);
    }

    @Test
    @DisplayName("유저 비밀번호를 변경할 때 현재 비밀번호가 틀리면 예외가 발생한다.")
    void 유저_비밀번호를_변경할_때_현재_비밀번호가_틀리면_예외가_발생한다() {
        long userId = 1L;
        String currentPassword = "correctPassword";
        String inCorrectPassword = "incorrectPassword";
        User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(currentPassword))
                .build();
        UpdateUserPasswordReq req = new UpdateUserPasswordReq(inCorrectPassword, "newPassword", "newPassword");
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));

        BusinessException businessException = assertThrows(BusinessException.class, () -> {
            userService.updateUserPassword(userId, req);
        });

        assertEquals(ErrorCode.INVALID_PASSWORD, businessException.getErrorCode());
    }

    @Test
    @DisplayName("유저 비밀번호를 변경할 때 확인 비밀번호가 틀리면 예외가 발생한다.")
    void 유저_비밀번호를_변경할_때_확인_비밀번호가_틀리면_예외가_발생한다() {
        long userId = 1L;
        String newPassword = "newPassword";
        String confirmNewPassword = "InCorrectNewPassword";
        User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode("password"))
                .build();
        UpdateUserPasswordReq req = new UpdateUserPasswordReq("passwor", confirmNewPassword, newPassword);
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));

        BusinessException businessException = assertThrows(BusinessException.class, () -> {
            userService.updateUserPassword(userId, req);
        });

        assertEquals(ErrorCode.INVALID_CONFIRM_NEW_PASSWORD, businessException.getErrorCode());
    }

    @Test
    @DisplayName("유저를 삭제할 수 있다.")
    void 유저를_삭제할_수_있다() {
        long userId = 1L;
        User user = User.builder()
                .userId(userId)
                .password(passwordEncoder.encode("password"))
                .build();
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));

        UserRes delete = userService.delete("", userId, userId);

        assertNotNull(delete.getDeletedAt());
    }
}
