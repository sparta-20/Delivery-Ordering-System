package com.delivery.domain.store.repository;

import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store,UUID>, JpaSpecificationExecutor<Store> {

    Optional<Store> findByStoreIdAndStatus(UUID storeId, StoreStatusEnum status);
    Page<Store> findAllByOwnerUserIdAndStatus(Long ownerUserId, StoreStatusEnum status, Pageable pageable);
    boolean existsByStoreIdAndOwnerUserIdAndDeletedAtIsNull(UUID storeId, Long ownerId);
}