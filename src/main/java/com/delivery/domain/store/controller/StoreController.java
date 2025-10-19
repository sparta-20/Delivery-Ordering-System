package com.delivery.domain.store.controller;
import com.delivery.domain.store.dto.StoreSearchCondition;
import com.delivery.domain.store.dto.StoreUpdateReq;
import com.delivery.domain.store.service.StoreService;
import com.delivery.domain.store.util.PageableUtils;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import com.delivery.domain.store.dto.StoreCreateReq;
import com.delivery.domain.store.dto.StoreRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
@Tag(name = "Store", description = "가게 API")
public class StoreController {

    private final StoreService storeService;

    // OWNER, MASTER - 가게 생성
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    @PostMapping
    public ResponseEntity<ApiRes<StoreRes>> createStore(
            @RequestBody @Valid StoreCreateReq requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        StoreRes responseDto = storeService.createStore(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiRes.success(responseDto));
    }

    // OWNER, MASTER - 가게 수정
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER','MANAGER')")
    @PutMapping("/{storeId}")
    public ResponseEntity<ApiRes<StoreRes>> updateStore(
            @PathVariable UUID storeId,
            @RequestBody @Valid StoreUpdateReq requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        StoreRes responseDto = storeService.updateStore(storeId, requestDto, user );
        return ResponseEntity.status(HttpStatus.OK).body(ApiRes.success(responseDto));
    }

    // OWNER, MASTER - 가게 삭제
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER','MANAGER')")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiRes<Void>> deleteStore(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        storeService.deleteStore(storeId, user);
        return ResponseEntity.noContent().build();
    }

    // OWNER, MASTER, MANAGER - 본인 가게 조회
    @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
    @GetMapping("/me")
    public ResponseEntity<ApiRes<Page<StoreRes>>> getMyStores(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable){
        Pageable p = PageableUtils.enforce(pageable);
        User user = userDetails.getUser();
        Page<StoreRes> result = storeService.getMyStores(user, p);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    // MASTER, MANAGER - 점주별 가게 조회
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @GetMapping("/masters/{ownerUserId}")
    public ResponseEntity<ApiRes<Page<StoreRes>>> getOwnerStores(
            @PathVariable Long ownerUserId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable){
        Pageable p = PageableUtils.enforce(pageable);
        User user = userDetails.getUser();
        Page<StoreRes> result = storeService.getOwnerStores(ownerUserId, user, p);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    // 가게 검색 및 조회
    @GetMapping
    public ResponseEntity<ApiRes<Page<StoreRes>>> getAllStores(
            @ModelAttribute StoreSearchCondition cond,
            Pageable pageable){
        Pageable p = PageableUtils.enforce(pageable);
        Page<StoreRes> result = storeService.getAllStores(cond, p);
        return ResponseEntity.ok(ApiRes.success(result));
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiRes<StoreRes>> getStore(@PathVariable UUID storeId){
        return ResponseEntity.ok(ApiRes.success(storeService.getStore(storeId)));
    }






}
