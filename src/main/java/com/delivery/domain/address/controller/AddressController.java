package com.delivery.domain.address.controller;

import com.delivery.domain.address.dto.AddressRes;
import com.delivery.domain.address.dto.CreateAddressReq;
import com.delivery.domain.address.dto.UpdateAddressReq;
import com.delivery.domain.address.service.AddressService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressRes>> createAddress(
            @RequestBody CreateAddressReq request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AddressRes address = addressService.createAddress(request, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressRes>>> getAddresses(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AddressRes> addresses = addressService.getAddresses(userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(addresses));
    }

    @PatchMapping("{addressId}")
    public ResponseEntity<ApiResponse<AddressRes>> updateAddress(
            @PathVariable UUID addressId,
            @RequestBody UpdateAddressReq request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        AddressRes address = addressService.updateAddress(addressId, request, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @DeleteMapping("{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        addressService.deleteAddress(addressId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("{addressId}/default")
    public ResponseEntity<ApiResponse<AddressRes>> setDefaultAddress(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AddressRes address = addressService.setDefaultAddress(addressId, userDetails.getUser());
        return ResponseEntity.ok(ApiResponse.success(address));
    }
}
