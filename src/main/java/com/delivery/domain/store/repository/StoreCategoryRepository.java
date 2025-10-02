package com.delivery.domain.store.repository;

import com.delivery.domain.store.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory,Integer> {
    boolean existsByName(String code);
}
