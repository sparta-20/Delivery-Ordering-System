package com.delivery.store.repository;

import com.delivery.store.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory,Integer> {
    boolean existsByName(String code);
}
