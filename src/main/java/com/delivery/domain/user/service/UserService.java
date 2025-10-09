package com.delivery.domain.user.service;

import com.delivery.domain.user.dto.UpdateUserRequest;
import com.delivery.domain.user.entity.User;

public interface UserService {
    User getUserById(Long userId);
    User updateUser(Long userId, UpdateUserRequest request);
}