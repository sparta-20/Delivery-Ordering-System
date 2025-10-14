package com.delivery.domain.user.service;

import com.delivery.domain.user.dto.UpdateUserPasswordReq;
import com.delivery.domain.user.dto.UpdateUserReq;
import com.delivery.domain.user.entity.User;

public interface UserService {
    User getUserById(Long userId);
    User updateUser(Long userId, UpdateUserReq request);
    User updateUserPassword(Long userId, UpdateUserPasswordReq request);
    User delete(Long requestUserId, Long userId);
}