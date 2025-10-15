package com.delivery.domain.review.controller;

import com.delivery.domain.review.dto.ReviewCreateReq;
import com.delivery.domain.review.dto.ReviewRes;
import com.delivery.domain.review.dto.ReviewUpdateReq;
import com.delivery.domain.review.service.ReviewService;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 생성
     * - 주문 완료(배송 완료) 후 작성 가능
     * - 주문당 1개의 리뷰만 작성 가능
     * - 평점 1~5점
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ReviewRes>> createReview(
            @Valid @RequestBody ReviewCreateReq request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewRes response = reviewService.createReview(userDetails.getUserId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    /**
     * 리뷰 조회
     * - CUSTOMER(작성자 본인), OWNER(본인 가게), MANAGER, MASTER 접근 가능
     * - 삭제되지 않은 리뷰만 조회
     */
    @GetMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<ApiResponse<ReviewRes>> getReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewRes response = reviewService.getReview(userDetails.getUserId(), userDetails.getRole(), reviewId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 리뷰 수정
     * - 작성자 본인만 수정 가능 (CUSTOMER만 허용)
     * - 수정 가능 필드: content, rating
     */
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ReviewRes>> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewUpdateReq request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewRes response = reviewService.updateReview(userDetails.getUserId(), reviewId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 리뷰 삭제 (Soft Delete)
     * - CUSTOMER: 본인이 작성한 리뷰만 삭제 가능
     * - MANAGER/MASTER: 모든 리뷰 삭제 가능
     */
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'MASTER')")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewService.deleteReview(userDetails.getUserId(), userDetails.getRole(), reviewId);

        return ResponseEntity.noContent().build();
    }
}