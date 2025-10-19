package com.delivery.domain.review.controller;

import com.delivery.domain.review.dto.*;
import com.delivery.domain.review.service.ReviewService;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Owner / Reviews", description = "가게 사장용 리뷰 관리 API")
@RestController
@RequestMapping("/api/v1/owner/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('OWNER')")
public class OwnerReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "내 가게 리뷰 목록",
            description = """
            사장 권한으로 특정 가게의 리뷰 목록을 조회합니다.
            
            - 삭제된 리뷰는 제외됩니다.
            - 평점 필터(rating)가 null이면 전체입니다.
            - 기본 정렬: createdAt DESC
            - 페이지 크기: 10/30/50만 허용(그 외는 10으로 강제)
            """
    )
    @GetMapping
    public ResponseEntity<ApiRes<Page<OwnerReviewDetailRes>>> getMyStoreReviews(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam UUID storeId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Page<OwnerReviewDetailRes> response = reviewService.getMyStoreReviews(userDetails.getUserId(), storeId, rating, page, size, direction);
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "내 가게 리뷰 단건 상세",
            description = """
            사장 권한으로 특정 리뷰를 상세 조회합니다.
            
            - 삭제된 리뷰는 조회되지 않습니다.
            - 소유권 검증(사장-가게)이 선행됩니다.
            """
    )
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiRes<OwnerReviewDetailRes>> getMyStoreReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam UUID storeId,
            @PathVariable UUID reviewId
    ) {
        OwnerReviewDetailRes response = reviewService.getMyStoreReviewDetail(userDetails.getUserId(), storeId, reviewId);
        return ResponseEntity.ok(ApiRes.success(response));
    }
}