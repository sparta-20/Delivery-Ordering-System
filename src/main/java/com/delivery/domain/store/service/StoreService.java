package com.delivery.domain.store.service;

import com.delivery.domain.store.dto.StoreCreateRequestDto;
import com.delivery.domain.store.dto.StoreResponseDto;
import com.delivery.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface StoreService {

    // 1. OWNER, MASTER - 가게 생성
    StoreResponseDto createStore(StoreCreateRequestDto requestDto, User user);

    // 2. OWNER, MASTER - 가게 수정

    // 3. OWNER, MASTER - 가게 삭제

    // 4. OWNER, MASTER - 본인 가게 조회

    // 5. 누구나 전체 가게 목록 조회
    Page<StoreResponseDto> getStores(int page, int size, String sortBy, boolean isAsc);

    // 6. 누구나 가게 상세 조회







}
