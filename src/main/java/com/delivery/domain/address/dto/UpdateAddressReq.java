package com.delivery.domain.address.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAddressReq {

    private String alias;

    private String detailAddress;
}
