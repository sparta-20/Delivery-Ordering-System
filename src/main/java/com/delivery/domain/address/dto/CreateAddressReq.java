package com.delivery.domain.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAddressReq {

    @NotBlank(message = "별칭은 필수 입력값입니다.")
    private String alias;

    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String detailAddress;

    @NotNull(message = "isDefault 값은 필수입니다.")
    private Boolean isDefault;
}
