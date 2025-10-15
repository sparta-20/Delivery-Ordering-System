package com.delivery.domain.store.repository;

import com.delivery.domain.store.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
    boolean existsByCategoryName(String categoryName);
    Optional<StoreCategory> findByCategoryNameAndIsActiveTrue(String categoryName);

}