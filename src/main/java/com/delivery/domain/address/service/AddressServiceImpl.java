package com.delivery.domain.address.service;

import com.delivery.domain.address.dto.AddressRes;
import com.delivery.domain.address.dto.CreateAddressReq;
import com.delivery.domain.address.dto.UpdateAddressReq;
import com.delivery.domain.address.entity.Address;
import com.delivery.domain.address.repository.AddressRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public AddressRes createAddress(CreateAddressReq request, User user) {
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.updateAllDefaultFalseByUser(user);
        }

        Address address = Address.builder()
                .alias(request.getAlias())
                .detailAddress(request.getDetailAddress())
                .isDefault(request.getIsDefault())
                .user(user)
                .build();

        addressRepository.save(address);
        return AddressRes.from(address);
    }

    @Override
    public List<AddressRes> getAddresses(User user) {
        return addressRepository.findAllByUser(user)
                .stream()
                .map(AddressRes::from)
                .toList();
    }

    @Override
    @Transactional
    public AddressRes updateAddress(UUID addressId, UpdateAddressReq request, User user) {
        Address address = addressRepository.findByAddressIdAndUser(addressId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        if (request.getAlias() != null && !request.getAlias().isBlank()) {
            address.updateAlias(request.getAlias());
        }
        if (request.getDetailAddress() != null && !request.getDetailAddress().isBlank()) {
            address.updateDetailAddress(request.getDetailAddress());
        }

        return AddressRes.from(address);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID addressId, User user) {
        Address address = addressRepository.findByAddressIdAndUser(addressId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        address.markDeleted(user.getUserId());
    }

    @Override
    @Transactional
    public AddressRes setDefaultAddress(UUID addressId, User user) {
        Address address = addressRepository.findByAddressIdAndUser(addressId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));

        addressRepository.updateAllDefaultFalseByUser(user);
        address.changeDefaultTrue();
        addressRepository.flush();

        return AddressRes.from(address);
    }
}
