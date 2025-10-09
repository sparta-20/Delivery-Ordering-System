package com.delivery.user.dto;

import com.delivery.user.entity.PublicStatus;
import com.delivery.user.entity.User;
import com.delivery.user.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String nickname;
    private String email;
    private UserRoleEnum role;
    private PublicStatus isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getRole(),
                user.getPublicStatus(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
