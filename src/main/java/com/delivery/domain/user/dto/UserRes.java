package com.delivery.domain.user.dto;

import com.delivery.domain.user.entity.PublicStatus;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserRes {
    private Long userId;
    private String nickname;
    private String email;
    private String phoneNumber;
    private UserRoleEnum role;
    private PublicStatus isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    public static UserRes from(User user) {
        return new UserRes(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getPublicStatus(),
                user.getCreatedAt(),
                user.getModifiedAt(),
                user.getDeletedAt()
        );
    }
}
