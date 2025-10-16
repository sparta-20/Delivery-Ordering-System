package com.delivery.domain.address.service;

import com.delivery.domain.address.dto.AddressRes;
import com.delivery.domain.address.dto.CreateAddressReq;
import com.delivery.domain.address.dto.UpdateAddressReq;
import com.delivery.domain.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressRes createAddress(CreateAddressReq request, User user);
    List<AddressRes> getAddresses(User user);
    AddressRes updateAddress(UUID addressId, UpdateAddressReq request, User user);
    void deleteAddress(UUID addressId, User user);
    AddressRes setDefaultAddress(UUID addressId, User user);
}
