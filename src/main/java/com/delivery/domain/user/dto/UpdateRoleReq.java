package com.delivery.domain.user.dto;

import com.delivery.domain.user.entity.UserRoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleReq {

    @NotNull
    private UserRoleEnum role;
}
