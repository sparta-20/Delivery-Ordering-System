package com.delivery.domain.store.service;

import com.delivery.domain.store.dto.StoreCreateRequestDto;
import com.delivery.domain.store.dto.StoreResponseDto;
import com.delivery.domain.store.dto.StoreUpdateRequestDto;
import com.delivery.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface StoreService {

    // OWNER, MASTER - 가게 생성
    StoreResponseDto createStore(StoreCreateRequestDto requestDto, User user);

    // OWNER, MASTER - 가게 수정
    StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto requestDto, User user);

    // OWNER, MASTER - 가게 삭제
    StoreResponseDto deleteStore(UUID storeId, User user);


    // OWNER, MASTER - 본인 가게 조회


    // MASTER - 점주별 가게 조회


    // 가게 검색


    // 전체 가게 목록 조회
    Page<StoreResponseDto> getStores(int page, int size, String sortBy, boolean isAsc);


    // 가게 상세 조회


    // 지역별 가게 조회


    // 카테고리별 가게 조회








}
