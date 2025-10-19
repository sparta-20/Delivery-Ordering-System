package com.delivery.domain.review.service;

import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.service.OrderService;
import com.delivery.domain.review.dto.*;
import com.delivery.domain.review.entity.Review;
import com.delivery.domain.review.repository.ReviewRepository;
import com.delivery.domain.store.service.StoreService;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.service.UserService;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import com.delivery.global.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final OrderService orderService;
    private final StoreService storeService;

    /** Customer */
    // 리뷰 생성
    @Override
    @Transactional
    public ReviewRes createReview(Long userId, ReviewCreateReq request) {
        // 사용자 조회
        User writer = userService.getUserById(userId);
        // 주문 조회
        Order order = orderService.getOrderById(request.getOrderId());
        // 검증
        validateOrderOwner(order, userId);
        validateOrderDone(order);
        validateDuplicateReview(request.getOrderId());
        // 리뷰 생성 및 저장
        Review savedReview = createAndSaveReview(
                writer,
                order,
                request.getRating(),
                request.getContent()
        );
        return ReviewRes.from(savedReview);
    }

    // 리뷰 단건 조회 (본인 리뷰만)
    @Override
    public CustomerReviewDetailRes getMyReview(Long userId, UUID reviewId) {
        return CustomerReviewDetailRes.from(getUserReviewDetail(userId, reviewId));
    }

    // 내 리뷰 전체 목록 조회 (최신순/과거순)
    @Override
    public Page<CustomerReviewDetailRes> getMyReviews(
            Long userId,
            int page,
            int size,
            Sort.Direction direction
    ) {
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        Page<Review> reviewPage = reviewRepository.searchMyReviews(userId, pageable);
        return reviewPage.map(CustomerReviewDetailRes::from);
    }

    // 리뷰 수정
    @Override
    @Transactional
    public ReviewRes updateReview(Long userId, UUID reviewId, ReviewUpdateReq request) {
        Review review = getUserReviewOrThrow(userId, reviewId);
        review.update(request.getRating(), request.getContent());
        return ReviewRes.from(review);
    }

    // 리뷰 삭제 (Soft Delete)
    @Override
    @Transactional
    public void deleteReview(Long userId, UUID reviewId) {
        Review review = getUserReviewOrThrow(userId, reviewId);
        review.markDeleted(userId);
    }

    /** Owner */
    @Override
    public Page<OwnerReviewDetailRes> getMyStoreReviews(
            Long ownerId, UUID storeId, Integer rating, int page, int size, Sort.Direction direction
    ) {
        storeService.validateStoreOwnership(ownerId, storeId);
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        Page<Review> reviewPage = reviewRepository.findStoreReviews(storeId, rating, pageable);
        return reviewPage.map(OwnerReviewDetailRes::from);
    }

    @Override
    public OwnerReviewDetailRes getMyStoreReviewDetail(Long ownerId, UUID storeId, UUID reviewId) {
        storeService.validateStoreOwnership(ownerId, storeId);
        Review review = findActiveReviewByStore(storeId, reviewId);
        return OwnerReviewDetailRes.from(review);
    }

    /** Admin */
    @Override
    public Page<AdminReviewDetailRes> searchReviewsForAdmin(
            Long userId, UUID storeId, Integer rating, boolean includeDeleted,
            int page, int size, Sort.Direction direction
    ) {
        Pageable pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        Page<Review> pageResult = reviewRepository.searchReviewsForAdmin(
                userId, storeId, rating, includeDeleted, pageable
        );
        return pageResult.map(AdminReviewDetailRes::from);
    }

    @Override
    public AdminReviewDetailRes getReviewDetailForAdmin(UUID reviewId, boolean includeDeleted) {
        Review review = getReviewForAdmin(reviewId, includeDeleted);
        return AdminReviewDetailRes.from(review);
    }

    @Override
    @Transactional
    public void deleteReviewByAdmin(Long userId,UUID reviewId) {
        Review review = getReviewById(reviewId);
        review.markDeleted(userId);
    }

    @Override
    @Transactional
    public AdminReviewDetailRes restoreReview(UUID reviewId) {
        Review review = getReviewForRestore(reviewId);
        review.restore();
        return AdminReviewDetailRes.from(review);
    }

    /** 공통 */
    @Override
    public Page<PublicReviewRes> getStoreReviews(UUID storeId, int page, int size, Sort.Direction direction) {
        var pageable = PageableUtils.createPageableWithCreatedAt(page, size, direction);
        Page<Review> reviews = reviewRepository.findPublicStoreReviews(storeId, pageable);
        return reviews.map(PublicReviewRes::from);
    }

    @Override
    public StoreReviewStats getStoreReviewStats(UUID storeId) {
        return reviewRepository.findStoreReviewStats(storeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REVIEW_STATS_NOT_FOUND));
    }

    // -------- Private Methods  --------
    // Customer
    // 리뷰 엔티티 생성 및 저장
    private Review createAndSaveReview(User user, Order order, int rating, String content) {
        Review review = Review.builder()
                .user(user)
                .store(order.getStore())
                .order(order)
                .rating(rating)
                .content(content)
                .build();
        return reviewRepository.save(review);
    }

    // 사용자 소유 리뷰 조회 (수정/삭제용) - Lazy Loading (연관 엔티티 미로딩)
    private Review getUserReviewOrThrow(Long userId, UUID reviewId) {
        return reviewRepository.findByReviewIdAndUser_UserIdAndDeletedAtIsNull(reviewId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND_OR_FORBIDDEN));
    }

    // 사용자 소유 리뷰 상세 조회 (조회용) - Fetch Join (User/Order/Store 즉시 로딩)
    public Review getUserReviewDetail(Long userId, UUID reviewId) {
        return reviewRepository.findUserReviewDetail(userId, reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    // Admin
    // 리뷰 단건 조회 - 범위: 유효한 (삭제용)
    private Review getReviewById(UUID reviewId) {
        return reviewRepository.findByReviewIdAndDeletedAtIsNull(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    // 리뷰 단건 조회 - 범위: 삭제 여부 선택 (삭제 리뷰 조회 가능)
    private Review getReviewForAdmin(UUID reviewId, boolean includeDeleted) {
        return reviewRepository.findReviewDetailForAdmin(reviewId, includeDeleted)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    // 리뷰 단건 조회 - 범위: 전체 (복구용)
    private Review getReviewForRestore(UUID reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    // 해당 가게 리뷰 단건 조회
    private Review findActiveReviewByStore(UUID storeId, UUID reviewId) {
        return reviewRepository.findStoreReviewDetail(storeId, reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    // -------- Validation  --------
    // 주문 소유권 검증 - 본인 주문인지
    private void validateOrderOwner(Order order, Long userId) {
        if (!order.isOwnedBy(userId)) {
            throw new BusinessException(ErrorCode.REVIEW_ORDER_NOT_OWNED);
        }
    }

    // 주문 완료 상태 검증 - 배송 완료(DONE) 상태만 리뷰 작성 가능
    private void validateOrderDone(Order order) {
        if (!order.isDone()) {
            throw new BusinessException(ErrorCode.ORDER_NOT_DONE);
        }
    }

    // 중복 리뷰 검증 - 주문당 1개의 리뷰만 작성 가능
    private void validateDuplicateReview(UUID orderId) {
        if (reviewRepository.existsByOrder_OrderIdAndDeletedAtIsNull(orderId)) {
            throw new BusinessException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }
    }
}