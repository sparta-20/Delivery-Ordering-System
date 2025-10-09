package com.delivery.domain.user.service.impl;

import com.delivery.domain.user.dto.UpdateUserRequest;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public User updateUser(Long userId, UpdateUserRequest request) {
        request.trim();
        User user = getUserById(userId);

        validateNickname(user.getNickname(), request.getNickname());
        validateEmail(user.getEmail(), request.getEmail());

        user.update(request.getNickname(), request.getEmail(), request.getPublicStatus());

        return user;
    }

    private void validateNickname(String userNickname, String newNickname) {
        if (Objects.equals(userNickname, newNickname)) return;

        if (userRepository.existsByNickname(newNickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private void validateEmail(String userEmail, String newEmail) {
        if (Objects.equals(userEmail,newEmail)) return;

        if (userRepository.existsByEmail(newEmail)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
