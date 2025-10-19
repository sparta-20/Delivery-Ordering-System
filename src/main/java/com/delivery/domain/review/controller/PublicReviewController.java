package com.delivery.domain.review.controller;

import com.delivery.domain.review.dto.PublicReviewRes;
import com.delivery.domain.review.dto.StoreReviewStats;
import com.delivery.domain.review.service.ReviewService;
import com.delivery.global.common.ApiRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Review", description = "리뷰 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores/{storeId}/reviews")
public class PublicReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "가게 리뷰 목록 (공개용)",
            description = """
                    특정 가게의 리뷰 목록을 공개적으로 조회합니다.
                    
                    - 삭제되지 않은 리뷰만 표시
                    - 기본 정렬: createdAt DESC
                    - page: 0부터 시작, size는 10/30/50만 허용
                    """
    )
    @GetMapping
    public ResponseEntity<ApiRes<Page<PublicReviewRes>>> getStoreReviews(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Page<PublicReviewRes> response = reviewService.getStoreReviews(storeId, page, size, direction);
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "가게 리뷰 통계 조회 (공개용)",
            description = """
                    특정 가게의 리뷰 통계를 조회합니다.
                    
                    **응답 정보**
                    - storeId: 가게 ID
                    - name: 가게 이름
                    - totalCount: 전체 리뷰 수
                    - avgRating: 평균 평점 (소수점 1~2자리)
                    """
    )
    @GetMapping("/stats")
    public ResponseEntity<ApiRes<StoreReviewStats>> getStoreReviewStats(@PathVariable UUID storeId) {
        StoreReviewStats response = reviewService.getStoreReviewStats(storeId);
        return ResponseEntity.ok(ApiRes.success(response));
    }
}

