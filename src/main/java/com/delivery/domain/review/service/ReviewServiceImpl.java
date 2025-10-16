package com.delivery.domain.review.service;

import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.entity.OrderStatusEnum;
import com.delivery.domain.order.service.OrderService;
import com.delivery.domain.review.dto.ReviewCreateReq;
import com.delivery.domain.review.dto.ReviewRes;
import com.delivery.domain.review.dto.ReviewSearchRes;
import com.delivery.domain.review.dto.ReviewUpdateReq;
import com.delivery.domain.review.entity.Review;
import com.delivery.domain.review.repository.ReviewRepository;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.service.StoreService;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final OrderService orderService;
    private final StoreService storeService;

    // 리뷰 생성
    @Override
    @Transactional
    public ReviewRes createReview(Long userId, ReviewCreateReq request) {
        log.info("[REVIEW] 생성 요청 - userId: {}, orderId: {}", userId, request.getOrderId());

        // 사용자 조회
        User user = userService.getUserById(userId);
        // 주문 조회
        Order order = orderService.getOrderById(request.getOrderId());

        // 검증
        validateReviewCreation(user, order);

        // 리뷰 저장
        Review savedReview = saveReview(
                user,
                order,
                order.getStore(),
                request.getRating(),
                request.getContent()
        );

        log.info("[REVIEW] 생성 완료 - reviewId: {}", savedReview.getReviewId());
        return ReviewRes.from(
                savedReview,
                order.getOrderId(),
                user.getUserId(),
                user.getNickname()
        );
    }

    // 리뷰 조회
    @Override
    public ReviewRes getReview(Long userId, UserRoleEnum role, UUID reviewId) {
        log.info("[REVIEW] 조회 요청 - reviewId: {}, userId: {}, role: {}", reviewId, userId, role);

        // 리뷰 조회 (User, Order 정보와 함께 조회)
        Review review = getReviewWithUserAndOrder(reviewId);

        // 접근 권한 검증
        validateReadPermission(userId, role, review);

        log.info("[REVIEW] 조회 완료 - reviewId: {}", reviewId);
        return ReviewRes.from(
                review,
                review.getOrder().getOrderId(),
                review.getUser().getUserId(),
                review.getUser().getNickname()
        );
    }

    /**
     * 조회 권한 검증
     * - MANAGER/MASTER: 모든 리뷰 조회 가능
     * - OWNER: 본인 가게 리뷰만 조회 가능
     * - CUSTOMER: 본인이 작성한 리뷰만 조회 가능
     */
    private void validateReadPermission(Long userId, UserRoleEnum role, Review review) {
        // 관리자는 모든 리뷰 조회 가능
        if (role == UserRoleEnum.MANAGER || role == UserRoleEnum.MASTER) {
            log.debug("[REVIEW] 관리자 권한으로 조회 - userId: {}, role: {}", userId, role);
            return;
        }

        // OWNER는 본인 가게 리뷰만 조회 가능
        if (role == UserRoleEnum.OWNER) {
            Long ownerId = review.getStore().getOwner().getUserId();
            if (!ownerId.equals(userId)) {
                throw new BusinessException(ErrorCode.REVIEW_READ_FORBIDDEN);
            }
            return;
        }

        // 일반 사용자는 본인 리뷰만 조회 가능
        if (!review.getUser().getUserId().equals(userId)) {
            log.warn("[REVIEW] 조회 권한 없음 - userId: {}, reviewOwnerId: {}",
                    userId, review.getUser().getUserId());
            throw new BusinessException(ErrorCode.REVIEW_READ_FORBIDDEN);
        }

        log.debug("[REVIEW] 본인 리뷰 조회 - userId: {}", userId);
    }

    @Override
    @Transactional
    public ReviewRes updateReview(Long userId, UUID reviewId, ReviewUpdateReq request) {
        log.info("[REVIEW] 수정 요청 - reviewId: {}, userId: {}", reviewId, userId);

        // 리뷰 조회 (User, Order 정보와 함께 조회)
        Review review = getReviewWithUserAndOrder(reviewId);

        // 작성자 본인 검증 (CUSTOMER만 수정 가능)
        validateUpdatePermission(userId, review);

        // 리뷰 수정
        review.update(request.getRating(), request.getContent());

        log.info("[REVIEW] 수정 완료 - reviewId: {}, rating: {}", reviewId, request.getRating());
        return ReviewRes.from(
                review,
                review.getOrder().getOrderId(),
                review.getUser().getUserId(),
                review.getUser().getNickname()
        );
    }

    /**
     * 수정 권한 검증
     * - 작성자 본인만 수정 가능 (CUSTOMER만 허용)
     */
    private void validateUpdatePermission(Long userId, Review review) {
        if (!review.getUser().getUserId().equals(userId)) {
            log.warn("[REVIEW] 수정 권한 없음 - userId: {}, reviewOwnerId: {}",
                    userId, review.getUser().getUserId());
            throw new BusinessException(ErrorCode.REVIEW_UPDATE_FORBIDDEN);
        }

        log.debug("[REVIEW] 본인 리뷰 수정 - userId: {}", userId);
    }

    // 리뷰 삭제 (Soft Delete)
    @Override
    @Transactional
    public void deleteReview(Long userId, UserRoleEnum role, UUID reviewId) {
        log.info("[REVIEW] 삭제 요청 - userId: {}, role: {}, reviewId: {}", userId, role, reviewId);

        // 리뷰 조회 (User 정보와 함께 조회 (JOIN FETCH))
        Review review = getReviewWithUser(reviewId);

        // 권한 검증
        validateDeletePermission(userId, role, review);

        // Soft Delete 처리
        review.markDeleted(userId);

        log.info("[REVIEW] 삭제 완료 - reviewId: {}, deletedBy: {}", reviewId, userId);
    }

    /**
     * 권한별 스코프
     * - CUSTOMER: storeId 필수(특정 가게만)
     * - OWNER   : 자신의 가게로 강제(요청 storeId 무시)
     * - MANAGER/MASTER: 요청값 그대로(null이면 전체)
     */
    @Override
    public Page<ReviewSearchRes> searchReviews(
            UUID storeId,
            int rating,
            Long writerId,
            int page,
            int size,
            Sort.Direction direction,
            User currentUser
    ) {
        log.info("[REVIEW_SEARCH] 검색 시작 - requesterId={}, role={}, storeId={}, rating={}, writerId={}, page={}, size={}, dir={}",
                currentUser.getUserId(), currentUser.getRole(), storeId, rating, writerId, page, size, direction);

        // rating 유효성 검증
        validateRating(rating);

        // Pageable 생성
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);

        // 권한에 따른 storeId 결정 (OWNER: 본인 가게 강제, CUSTOMER/MANAGER/MASTER: 요청 또는 전체)
        UUID resolvedStoreId = determineSearchStoreId(storeId, currentUser);
        // 권한에 따른 작성자 강제 (CUSTOMER는 storeId 유무에 따라 정책 분기)
        Long resolvedWriterId = determineSearchUserId(storeId, writerId, currentUser);

        log.debug("[REVIEW_SEARCH] 조회 범위 적용 - resolvedStoreId={}, resolvedWriterId={}",
                resolvedStoreId != null ? resolvedStoreId : "ALL",
                resolvedWriterId != null ? resolvedWriterId : "ALL");

        // 검색 실행 (DB 조회)
        try {
            Page<Review> reviewPage = reviewRepository.searchReviews(resolvedStoreId, rating, resolvedWriterId, pageable);

            log.info("[REVIEW_SEARCH] 검색 완료 - total={}, pageNo={}",
                    reviewPage.getTotalElements(), reviewPage.getNumber());

            return reviewPage.map(ReviewSearchRes::from);

        } catch (DataAccessException dae) {
            log.error("[REVIEW_SEARCH] DB 조회 실패 - storeId={}, rating={}, writerId={}, page={}, size={}, dir={}",
                    resolvedStoreId, rating, resolvedWriterId, page, size, direction, dae);
            throw new BusinessException(ErrorCode.REVIEW_SEARCH_FAILED);
        }
    }

    /**
     * 권한에 따른 검색 storeId 결정
     * - CUSTOMER: 파라미터 그대로 (null이면 전체 검색)
     * - OWNER: 본인 가게로 강제 (파라미터 무시)
     * - MANAGER/MASTER: 파라미터 그대로 (null이면 전체 검색)
     */
    private UUID determineSearchStoreId(UUID requestedStoreId, User currentUser) {
        if (currentUser.getRole() == UserRoleEnum.OWNER) {
            // OWNER는 본인 가게만 조회 가능
            Store ownerStore = storeService.getStoreByOwnerId(currentUser.getUserId());
            return ownerStore.getStoreId(); // 요청값 무시
        }
        // CUSTOMER, MANAGER, MASTER: 요청 그대로 (null이면 전역)
        return requestedStoreId;
    }

    /**
     * 권한에 따른 검색 writerId 결정
     * - CUSTOMER:
     *   - storeId == null → 전역 "내 리뷰" (writerId = 본인)
     *   - storeId != null → 특정 가게
     *       - requestedWriterId == 본인 → 본인 리뷰만
     *       - requestedWriterId == null or 타인 → 가게 전체 리뷰 (writerId = null)
     * - OWNER / MANAGER / MASTER: 요청값 그대로 사용
     */
    private Long determineSearchUserId(UUID requestedStoreId, Long requestedWriterId, User currentUser) {
        UserRoleEnum role = currentUser.getRole();

        if (role == UserRoleEnum.CUSTOMER) {
            // 전역(모든 가게) 조회: 내 리뷰만
            if (requestedStoreId == null) {
                return currentUser.getUserId();
            }
            // 특정 가게 조회
            if (requestedWriterId != null && requestedWriterId.equals(currentUser.getUserId())) {
                return currentUser.getUserId(); // 본인 리뷰만
            }
            return null; // 가게 전체 리뷰
        }

        // OWNER / MANAGER / MASTER
        return requestedWriterId;
    }

    // 평점 유효성 검증 (1~5점)
    private void validateRating(Integer rating) {
        if (rating != null && (rating < 1 || rating > 5)) {
            log.warn("[REVIEW_SEARCH] 잘못된 평점 요청 - rating={}", rating);
            throw new BusinessException(ErrorCode.INVALID_REVIEW_RATING);
        }
    }

    /**
     * 삭제 권한 검증
     * - MANAGER/MASTER: 모든 리뷰 삭제 가능
     * - CUSTOMER: 본인 리뷰만 삭제 가능
     */
    private void validateDeletePermission(Long userId, UserRoleEnum role, Review review) {
        // 관리자는 모든 리뷰 삭제 가능
        if (role == UserRoleEnum.MANAGER || role == UserRoleEnum.MASTER) {
            log.debug("[REVIEW] 관리자 권한으로 삭제 - userId: {}, role: {}", userId, role);
            return;
        }

        // 일반 사용자는 본인 리뷰만 삭제 가능
        if (!review.getUser().getUserId().equals(userId)) {
            log.warn("[REVIEW] 권한 없음 - userId: {}, reviewOwnerId: {}",
                    userId, review.getUser().getUserId());
            throw new BusinessException(ErrorCode.REVIEW_DELETE_FORBIDDEN);
        }

        log.debug("[REVIEW] 본인 리뷰 삭제 - userId: {}", userId);
    }

    // 리뷰 조회 (삭제되지 않은 리뷰만, User 정보 함께 조회)
    private Review getReviewWithUser(UUID reviewId) {
        return reviewRepository.findByReviewIdWithUser(reviewId)
                .orElseThrow(() -> {
                    log.warn("[REVIEW] 리뷰 조회 실패 - reviewId: {}", reviewId);
                    return new BusinessException(ErrorCode.REVIEW_NOT_FOUND);
                });
    }

    // 리뷰 조회 (삭제되지 않은 리뷰만, User, Order 정보 함께 조회)
    private Review getReviewWithUserAndOrder(UUID reviewId) {
        return reviewRepository.findByReviewIdWithUserAndOrder(reviewId)
                .orElseThrow(() -> {
                    log.warn("[REVIEW] 리뷰 조회 실패 - reviewId: {}", reviewId);
                    return new BusinessException(ErrorCode.REVIEW_NOT_FOUND);
                });
    }

    // 리뷰 조회 (삭제되지 않은 리뷰만)
    private Review getReviewById(UUID reviewId) {
        return reviewRepository.findByReviewIdAndDeletedAtIsNull(reviewId)
                .orElseThrow(() -> {
                    log.warn("[REVIEW] 리뷰 조회 실패 - reviewId: {}", reviewId);
                    return new BusinessException(ErrorCode.REVIEW_NOT_FOUND);
                });
    }

    // 리뷰 저장
    private Review saveReview(User user, Order order, Store store, int rating, String content) {
        Review review = Review.builder()
                .user(user)
                .store(store)
                .order(order)
                .rating(rating)
                .content(content)
                .build();

        return reviewRepository.save(review);
    }

    private void validateReviewCreation(User user, Order order) {
        // 주문 소유권 검증
        validateOrderOwnership(order, user);
        // 주문 상태 검증 (배송 완료 여부)
        validateOrderStatus(order);
        // 중복 리뷰 여부 검증
        validateDuplicateReview(order.getOrderId());
    }

    // 주문 소유권 검증 (본인의 주문인지 확인)
    private void validateOrderOwnership(Order order, User user) {
        if (!Objects.equals(order.getUser().getUserId(), user.getUserId())) {
            log.warn("[REVIEW] 소유권 검증 실패 - userId: {}, orderUserId: {}",
                    user.getUserId(), order.getUser().getUserId());
            throw new BusinessException(ErrorCode.REVIEW_ORDER_NOT_OWNED);
        }
    }

    // 주문 상태 검증 (배송 완료 여부)
    private void validateOrderStatus(Order order) {
        if (order.getStatus() != OrderStatusEnum.DONE) {
            log.warn("[REVIEW] 주문 상태 불일치 - orderId: {}, status: {}, required: DONE",
                    order.getOrderId(), order.getStatus());
            throw new BusinessException(ErrorCode.ORDER_NOT_COMPLETED);
        }
    }

    // 중복 리뷰 검증 (주문당 1개의 리뷰만 가능)
    private void validateDuplicateReview(UUID orderId) {
        if (reviewRepository.existsByOrder_OrderIdAndDeletedAtIsNull(orderId)) {
            log.warn("[REVIEW] 중복 리뷰 방지 - orderId: {}", orderId);
            throw new BusinessException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }
    }
}