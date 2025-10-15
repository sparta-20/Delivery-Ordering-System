package com.delivery.domain.user.service;

import com.delivery.domain.user.dto.UpdateUserPasswordReq;
import com.delivery.domain.user.dto.UpdateUserReq;
import com.delivery.domain.user.dto.UserRes;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;

public interface UserService {
    User getUserById(Long userId);
    UserRes getUserResById(Long userId);
    UserRes updateUser(Long userId, UpdateUserReq request);
    UserRes updateUserPassword(Long userId, UpdateUserPasswordReq request);
    UserRes delete(Long requestUserId, Long userId);
    UserRes updateUserRole(Long requesterId, Long userId, UserRoleEnum role);
}