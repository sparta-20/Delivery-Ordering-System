package com.delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class StoreCreateReq {

    @NotBlank(message = "가게명은 필수입니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @NotBlank(message = "도시는 필수입니다.")
    private String city;

    @NotBlank(message = "구/군은 필수입니다.")
    private String district;

    @NotNull(message = "최소 주문 금액은 필수입니다.")
    private Integer minPrice;

    @NotNull(message = "카테고리는 필수입니다.")
    private UUID categoryId;

}