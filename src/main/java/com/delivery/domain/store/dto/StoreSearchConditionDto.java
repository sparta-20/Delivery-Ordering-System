package com.delivery.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class StoreSearchConditionDto {

    // 키워드 검색
    private final String keyword;

    // 지역 필터
    private final String city;
    private final String district;

    // 카테고리 필터
    private final UUID categoryId;

    // MASTER - 점주 필터
    private final Long ownerUserId;
}
