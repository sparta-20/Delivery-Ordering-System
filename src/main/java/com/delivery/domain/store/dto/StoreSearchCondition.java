package com.delivery.domain.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder
@Setter
public class StoreSearchCondition {

    // 키워드 검색
    private String keyword;

    // 지역 필터
    private String city;
    private String district;
    private String dong;

    // 카테고리 필터
    private UUID categoryId;
    private String categoryName;

    // MASTER - 점주 필터
    private final Long ownerUserId;

}
