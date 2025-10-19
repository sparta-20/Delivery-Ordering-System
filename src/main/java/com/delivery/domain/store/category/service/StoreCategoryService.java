package com.delivery.domain.store.category.service;

import com.delivery.domain.store.category.dto.StoreCategoryRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreCategoryService {
    Page<StoreCategoryRes> getAllCategories(Pageable pageable);
}
