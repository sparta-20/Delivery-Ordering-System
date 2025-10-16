package com.delivery.domain.address.dto;

import com.delivery.domain.address.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AddressRes {
    private UUID addressId;
    private String alias;
    private String detailAddress;
    private Boolean isDefault;

    public static AddressRes from(Address address) {
        return new AddressRes(
                address.getAddressId(),
                address.getAlias(),
                address.getDetailAddress(),
                address.getIsDefault()
        );
    }
}
