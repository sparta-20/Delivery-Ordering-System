package com.delivery.domain.store.category.controller;

import com.delivery.domain.store.category.dto.StoreCategoryRes;
import com.delivery.domain.store.category.service.StoreCategoryService;
import com.delivery.domain.store.util.PageableUtils;
import com.delivery.global.common.ApiRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class StoreCategoryController {

    private final StoreCategoryService storeCategoryService;

    @GetMapping
    public ResponseEntity<ApiRes<Page<StoreCategoryRes>>> getAllCategories(Pageable pageable){
        Pageable p = PageableUtils.enforce(pageable);
        Page<StoreCategoryRes> result = storeCategoryService.getAllCategories(p);
        return ResponseEntity.ok(ApiRes.success(result));
    }
}
