package com.delivery.domain.store.category.service;

import com.delivery.domain.store.category.dto.StoreCategoryRes;
import com.delivery.domain.store.category.repository.StoreCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreCategoryServiceImpl implements StoreCategoryService {

    private final StoreCategoryRepository storeCategoryRepository;

    @Override
    public Page<StoreCategoryRes> getAllCategories(Pageable pageable) {
        return storeCategoryRepository.findAllByIsActiveTrue(pageable).map(StoreCategoryRes::from);
    }
}
