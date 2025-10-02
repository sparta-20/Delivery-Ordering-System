package com.delivery.store.service;

import com.delivery.store.dto.StoreCreateRequestDto;
import com.delivery.store.dto.StoreResponseDto;
import com.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface storeService {

    // 1. OWNER, MASTER - 가게 생성
    StoreResponseDto createStore(StoreCreateRequestDto requestDto, User user);

    // 2. OWNER, MASTER - 가게 수정

    // 3. OWNER, MASTER - 가게 삭제

    // 4. 누구나 전체 가게 목록 조회

    // 5. 누구나 가게 상세 조회

    // 6. OWNER, MASTER - 본인 가게 조회







}
