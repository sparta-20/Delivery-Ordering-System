package com.delivery.store.repository;

import com.delivery.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store,UUID> {

    // 소프트 삭제 제외한 가게 단건 조회
    Optional<Store> findByIdAndDeletedAtIsNull(UUID id);

    // 카테고리별 가게 조회
    List<Store> findByCategoryIdAndDeletedAtIsNull(Integer categoryId);

    // 지역별 가게 조회
    List<Store> findByCityAndDistrictAndDeletedAtIsNull(String city, String district);

    // 점주가 등록한 가게들 조회
    List<Store> findByOwnerUserIdAndDeletedAtIsNull(Long ownerUserId);
}
