package com.delivery.domain.user.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserPasswordRequest {

    @NotNull
    private String currentPassword;

    @NotNull
    private String newPassword;

    @NotNull
    private String confirmNewPassword;
}
