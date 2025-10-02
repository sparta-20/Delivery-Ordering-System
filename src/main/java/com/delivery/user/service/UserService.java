package com.delivery.user.service;

import com.delivery.user.entity.User;

public interface UserService {
    User getUserIfAccessible(Long requesterId, Long userId);
}