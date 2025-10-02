package com.delivery.store.controller;

import com.delivery.security.UserDetailsImpl;
import com.delivery.store.dto.StoreCreateRequestDto;
import com.delivery.store.dto.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreController storeService;

    // 1. OWNER, MASTER - 가게 생성
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER')")
    @PostMapping
    public StoreResponseDto createStore(@RequestBody StoreCreateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return storeService.createStore(requestDto, userDetails.getUser());
    }

    // 2. OWNER, MASTER - 가게 수정

    // 3. OWNER, MASTER - 가게 삭제

    // 4. 누구나 전체 가게 목록 조회

    // 5. 누구나 가게 상세 조회

    // 6. OWNER, MASTER - 본인 가게 조회

}
