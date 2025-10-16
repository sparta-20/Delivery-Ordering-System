package com.delivery.domain.store.category.repository;

import com.delivery.domain.store.category.entity.StoreCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
    boolean existsByCategoryName(String categoryName);
    Optional<StoreCategory> findByCategoryNameAndIsActiveTrue(String categoryName);

    Page<StoreCategory> findAllByIsActiveTrue(Pageable pageable);


}