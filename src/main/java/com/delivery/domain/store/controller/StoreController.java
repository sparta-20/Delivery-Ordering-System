package com.delivery.domain.store.controller;
import com.delivery.domain.store.dto.StoreUpdateRequestDto;
import com.delivery.domain.store.service.StoreService;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.UserDetailsImpl;
import com.delivery.domain.store.dto.StoreCreateRequestDto;
import com.delivery.domain.store.dto.StoreResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    // OWNER, MASTER - 가게 생성
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER')")
    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponseDto>> createStore(
            @RequestBody @Valid StoreCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        StoreResponseDto responseDto = storeService.createStore(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }

    // OWNER, MASTER - 가게 수정
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER')")
    @PutMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> updateStore(
            @PathVariable UUID storeId,
            @RequestBody @Valid StoreUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        StoreResponseDto responseDto = storeService.updateStore(storeId, requestDto, user );
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));
    }

    // OWNER, MASTER - 가게 삭제
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER')")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Void>> deleteStore(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        storeService.deleteStore(storeId, user);
        return ResponseEntity.noContent().build();
    }




    //가게 검색 => 서치 기능에는 10건, 30건, 50건 기준으로 페이지에 노출됨. 기본은 10건, 생성일순


    // 5. 전체 가게 목록 조회 => pageable로 만들기...
    @GetMapping
    public Page<StoreResponseDto> getStores(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ) {
        return storeService.getStores(page - 1, size, sortBy, isAsc);
    }

    // 6. 가게 상세 조회

    // 지역별 가게 조회

    // 카테고리별 가게 조회


}


