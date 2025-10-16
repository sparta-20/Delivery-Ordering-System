package com.delivery.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpReq {

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(
            regexp = "^[a-z0-9]{4,10}$",
            message = "닉네임은 4~10자의 알파벳 소문자와 숫자만 가능합니다."
    )
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:;<>?,./]).{8,15}$",
            message = "비밀번호는 8~15자, 대소문자, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "전화번호는 필수 값입니다.")
    @Size(min = 10, max = 11, message = "전화번호는 10~11자리 숫자여야 합니다.")
    @Pattern(regexp = "^[0-9]+$", message = "전화번호는 숫자만 입력 가능합니다.")
    private String phoneNumber;
}
