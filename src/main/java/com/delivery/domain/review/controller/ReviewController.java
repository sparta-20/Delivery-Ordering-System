package com.delivery.domain.review.controller;

import com.delivery.domain.review.dto.ReviewCreateReq;
import com.delivery.domain.review.dto.ReviewRes;
import com.delivery.domain.review.service.ReviewService;
import com.delivery.global.common.ApiResponse;
import com.delivery.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        ReviewRes response = reviewService.createReview(userDetails.getUser().getUserId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}