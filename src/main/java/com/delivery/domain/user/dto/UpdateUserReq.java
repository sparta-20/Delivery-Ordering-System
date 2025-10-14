package com.delivery.domain.user.dto;

import com.delivery.domain.user.entity.PublicStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserReq {

    @Size(min = 2, max = 20, message = "닉네임은 2~20자여야 합니다.")
    @NotBlank
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private PublicStatus publicStatus;

    public void trim() {
        setNickname(getNickname().trim());
        setEmail(getEmail().trim());
    }
}