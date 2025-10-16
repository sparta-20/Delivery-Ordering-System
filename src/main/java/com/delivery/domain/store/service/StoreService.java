package com.delivery.domain.store.service;

import com.delivery.domain.store.dto.StoreCreateReq;
import com.delivery.domain.store.dto.StoreRes;
import com.delivery.domain.store.dto.StoreSearchCondition;
import com.delivery.domain.store.dto.StoreUpdateReq;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreStatusEnum;
import com.delivery.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StoreService {

    // OWNER, MASTER - 가게 생성
    StoreRes createStore(StoreCreateReq requestDto, User user);

    // OWNER, MASTER - 가게 수정
    StoreRes updateStore(UUID storeId, StoreUpdateReq requestDto, User user);

    // OWNER, MASTER - 가게 삭제
    StoreRes deleteStore(UUID storeId, User user);

    // OWNER, MASTER - 본인 가게 조회
    Page<StoreRes> getMyStores(User user, Pageable pageable);

    // MASTER - 점주별 가게 조회
    Page<StoreRes> getOwnerStores(Long ownerUserId, User user, Pageable pageable);

    // 가게 검색 및 조회
    Page<StoreRes> getAllStores(StoreSearchCondition cond, Pageable pageable);

    // 가게 단건 조회
    StoreRes getStore(UUID storeId);

    Store getByStoreIdAndStatus(UUID id, StoreStatusEnum storeStatusEnum);

    Store getStoreByOwnerId(Long userId);
}