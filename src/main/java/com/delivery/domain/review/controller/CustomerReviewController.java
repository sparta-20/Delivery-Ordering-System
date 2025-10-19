package com.delivery.domain.review.controller;

import com.delivery.domain.review.dto.*;
import com.delivery.domain.review.service.ReviewService;
import com.delivery.global.common.ApiRes;
import com.delivery.global.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Review", description = "리뷰 관리 API")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER')")
public class CustomerReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "리뷰 작성 (고객용)",
            description = """
            주문 완료(배송 완료)된 주문에 대해 리뷰를 작성합니다.
            
            **제약사항:**
            - 본인의 주문에만 작성 가능
            - 배송 완료(DONE) 상태인 주문만 가능
            - 주문당 1개의 리뷰만 작성 가능
            - 평점은 1~5점 사이
            """
    )
    @PostMapping
    public ResponseEntity<ApiRes<ReviewRes>> createReview(
            @Valid @RequestBody ReviewCreateReq request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewRes response = reviewService.createReview(userDetails.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiRes.success(response));
    }

    @Operation(
            summary = "내 리뷰 단건 조회 (고객용)",
            description = """
            작성한 리뷰의 상세 정보를 조회합니다.
            
            **응답 정보:**
            - 리뷰 기본 정보 (평점, 내용, 작성일시)
            - 가게 정보 (가게명)
            - 주문 정보 (주문 일시)
            
            **참고:**
            - 본인이 작성한 리뷰만 조회 가능
            - 삭제된 리뷰는 조회 불가
            """
    )
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiRes<CustomerReviewDetailRes>> getMyReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CustomerReviewDetailRes response = reviewService.getMyReview(userDetails.getUserId(), reviewId);
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "내 리뷰 목록 조회 (고객용)",
            description = """
            본인이 작성한 모든 리뷰 목록을 조회합니다.
            
            **기본 정렬:** 최신순 (작성일시 기준)
            
            **페이징 파라미터:**
            - page: 페이지 번호 (0부터 시작, 기본값: 0)
            - size: 페이지 크기 (기본값: 10)
            - direction: 정렬 방향 (DESC: 최신순, ASC: 오래된순)
            """
    )
    @GetMapping("/me")
    public ResponseEntity<ApiRes<Page<CustomerReviewDetailRes>>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<CustomerReviewDetailRes> response = reviewService.getMyReviews(
                userDetails.getUserId(),
                page,
                size,
                direction
        );
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "리뷰 수정 (고객용)",
            description = """
            작성한 리뷰의 평점과 내용을 수정합니다.
            
            **수정 가능 항목:**
            - 평점 (rating): 1~5점
            - 내용 (content)
            
            **제약사항:**
            - 본인이 작성한 리뷰만 수정 가능
            - 삭제된 리뷰는 수정 불가
            """
    )
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiRes<ReviewRes>> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewUpdateReq request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewRes response = reviewService.updateReview(userDetails.getUserId(), reviewId, request);
        return ResponseEntity.ok(ApiRes.success(response));
    }

    @Operation(
            summary = "리뷰 삭제 (고객용)",
            description = """
            작성한 리뷰를 삭제합니다. (Soft Delete)
            
            **동작 방식:**
            - 실제로 데이터가 삭제되지 않고 deletedAt 필드가 설정됨
            - 삭제된 리뷰는 조회 및 수정 불가
            
            **제약사항:**
            - 본인이 작성한 리뷰만 삭제 가능
            """
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewService.deleteReview(userDetails.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }
}