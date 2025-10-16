package com.delivery.domain.user.dto;

import com.delivery.domain.user.entity.PublicStatus;
import jakarta.validation.constraints.*;
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

    @NotNull
    private PublicStatus publicStatus;

    @NotBlank(message = "전화번호는 필수 값입니다.")
    @Size(min = 10, max = 11, message = "전화번호는 10~11자리 숫자여야 합니다.")
    @Pattern(regexp = "^[0-9]+$", message = "전화번호는 숫자만 입력 가능합니다.")
    private String phoneNumber;

    public void trim() {
        setNickname(getNickname().trim());
        setEmail(getEmail().trim());
    }
}