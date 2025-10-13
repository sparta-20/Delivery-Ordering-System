package com.delivery.domain.store.service;

import com.delivery.domain.store.dto.StoreCreateReq;
import com.delivery.domain.store.dto.StoreRes;
import com.delivery.domain.store.dto.StoreUpdateReq;
import com.delivery.domain.user.entity.User;
import java.util.UUID;

public interface StoreService {

    // OWNER, MASTER - 가게 생성
    StoreRes createStore(StoreCreateReq requestDto, User user);

    // OWNER, MASTER - 가게 수정
    StoreRes updateStore(UUID storeId, StoreUpdateReq requestDto, User user);

    // OWNER, MASTER - 가게 삭제
    StoreRes deleteStore(UUID storeId, User user);


    // OWNER, MASTER - 본인 가게 조회


    // MASTER - 점주별 가게 조회


    // 가게 검색


    // 전체 가게 목록 조회



    // 가게 상세 조회


    // 지역별 가게 조회


    // 카테고리별 가게 조회








}
