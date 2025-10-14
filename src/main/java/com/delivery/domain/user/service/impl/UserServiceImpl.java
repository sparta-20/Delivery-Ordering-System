package com.delivery.domain.user.service.impl;

import com.delivery.domain.user.dto.UpdateUserPasswordReq;
import com.delivery.domain.user.dto.UpdateUserReq;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public User updateUser(Long userId, UpdateUserReq request) {
        request.trim();
        User user = getUserById(userId);

        validateNickname(user.getNickname(), request.getNickname());
        validateEmail(user.getEmail(), request.getEmail());

        user.update(request.getNickname(), request.getEmail(), request.getPublicStatus());
        return user;
    }

    @Override
    @Transactional
    public User updateUserPassword(Long userId, UpdateUserPasswordReq request) {
        User user = getUserById(userId);
        validateConfirmNewPassword(request.getNewPassword(), request.getConfirmNewPassword());
        validatePassword(request.getCurrentPassword(), user.getPassword());

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        return user;
    }

    @Override
    @Transactional
    public User delete(Long requestUserId, Long userId) {
        User user = getUserById(userId);
        user.markDeleted(requestUserId);
        return user;
    }

    private void validatePassword(String requestPassword, String useerPassword) {
        //현재 비밀번호 검증: 현재 비밀번호와 일치 하는가
        if (!passwordEncoder.matches(requestPassword, useerPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private void validateConfirmNewPassword(String newPassword, String confirmNewPassword) {
        // 확인&변경 비밀번호 검증: 확인 비밀번호와 변경 비밀번호가 일치 하는가
        if (!newPassword.equals(confirmNewPassword)) {
            throw new BusinessException(ErrorCode.INVALID_CONFIRM_NEW_PASSWORD);
        }
    }

    private void validateNickname(String userNickname, String newNickname) {
        if (Objects.equals(userNickname, newNickname)) return;

        if (userRepository.existsByNickname(newNickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private void validateEmail(String userEmail, String newEmail) {
        if (Objects.equals(userEmail, newEmail)) return;

        if (userRepository.existsByEmail(newEmail)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
