package com.delivery.domain.address.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAddressReq {

    private String alias;

    private String detailAddress;
}
