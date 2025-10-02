package com.delivery.user.service;

import com.delivery.exception.BusinessException;
import com.delivery.exception.ErrorCode;
import com.delivery.user.dto.UserResponse;
import com.delivery.user.entity.User;
import com.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public User getUserIfAccessible(Long requesterId, Long userId) {
        if(!requesterId.equals(userId)){
            throw new BusinessException(ErrorCode.USER_ACCESS_DENIED);
        }

        return getActivityUser(userId);
    }

    private User getActivityUser(Long userId) {
        return userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACTIVITY_USER_NOT_FOUND));
    }
}