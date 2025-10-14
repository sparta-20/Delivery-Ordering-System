package com.delivery.domain.store.repository;

import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store,UUID> {

    Optional<Store> findByStoreIdAndStatus(UUID storeId, StoreStatusEnum status);
    Page<Store> findAllByOwnerUserIdAndStatus(Long ownerUserId, StoreStatusEnum status, Pageable pageable);
}