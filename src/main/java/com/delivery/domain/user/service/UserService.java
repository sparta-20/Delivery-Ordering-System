package com.delivery.domain.user.service;

import com.delivery.domain.user.entity.User;

public interface UserService {
    User getUserById(Long userId);
}