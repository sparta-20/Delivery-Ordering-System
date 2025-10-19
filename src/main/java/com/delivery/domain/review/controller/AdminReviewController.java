package com.delivery.domain.review.controller;

import com.delivery.domain.review.dto.AdminReviewDetailRes;
import com.delivery.domain.review.service.ReviewService;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Review", description = "리뷰 관리 API")
@RestController
@RequestMapping("/api/v1/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
public class AdminReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "전체 리뷰 목록 조회 (관리자용)",
            description = """
            관리자 권한으로 모든 리뷰 목록을 조회합니다.
                                    
            **검색 필터:**
            - storeId: 특정 가게의 리뷰만 조회
            - userId: 특정 사용자의 리뷰만 조회
            - rating: 특정 평점만 조회
            - includeDeleted: 삭제된 리뷰 포함 여부
            
            **정렬:**
            - 기본: 최신순 (createdAt DESC)
            - page: 0부터 시작
            - size: 10, 30, 50만 허용 (그 외는 10으로 강제)
            """
    )
    @GetMapping
    public ResponseEntity<ApiRes<Page<AdminReviewDetailRes>>> getAllReviews(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) UUID storeId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "false") boolean includeDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Page<AdminReviewDetailRes> response = reviewService.searchReviewsForAdmin(
                userId, storeId, rating, includeDeleted, page, size, direction);
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "리뷰 상세 조회 (관리자용)",
            description = """
            특정 리뷰의 상세 정보를 조회합니다.
            
            **포함 정보:**
            - 리뷰 정보
            - 작성자 정보 (ID, 이름, 이메일)
            - 가게 정보 (ID, 가게명)
            - 주문 정보 (ID, 주문일시, 주문금액)
            - 삭제 정보 (삭제 여부, 삭제 일시, 삭제자)
            
            **정책:**
            - includeDeleted=true 인 경우 삭제된 리뷰도 조회 가능
            """
    )
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiRes<AdminReviewDetailRes>> getReview(
            @PathVariable UUID reviewId,
            @RequestParam(defaultValue = "true") boolean includeDeleted

    ) {
        AdminReviewDetailRes response = reviewService.getReviewDetailForAdmin(reviewId, includeDeleted);
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "리뷰 삭제 (관리자용)",
            description = """
            부적절한 리뷰를 관리자 권한으로 삭제합니다. (Soft Delete)
            
            **사유 예시:**
            - 욕설, 비방 등 부적절한 내용
            - 허위 리뷰
            - 광고성 내용
            - 기타 운영 정책 위반
            
            **정책:**
            - 삭제 시 deletedAt, deletedBy 필드 기록
            - 실제 데이터 보존
            """
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewService.deleteReviewByAdmin(userDetails.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "삭제된 리뷰 복구 (관리자용)",
            description = """
            삭제된 리뷰를 복구합니다. (Soft Delete 복구)
            
            **정책:**
            - 삭제된 리뷰만 복구 대상
            - 이미 복구된 리뷰에 대해 호출해도 문제없음
            - 복구 시 deletedAt, deletedBy 필드 초기화
            """
    )
    @PostMapping("/{reviewId}/restore")
    public ResponseEntity<ApiRes<AdminReviewDetailRes>> restoreReview(
            @PathVariable UUID reviewId
    ) {
        AdminReviewDetailRes response = reviewService.restoreReview(reviewId);
        return ResponseEntity.ok(ApiRes.success(response));
    }
}