package com.delivery.domain.user.dto;

import com.delivery.domain.user.entity.PublicStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
    @NotNull
    private String nickname;

    @Email
    @NotNull
    private String email;

    @NotNull
    private PublicStatus publicStatus;

    public void trim() {
        setNickname(getNickname().trim());
        setEmail(getEmail().trim());
    }
}