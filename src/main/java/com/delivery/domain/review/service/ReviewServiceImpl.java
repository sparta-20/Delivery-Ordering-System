package com.delivery.domain.review.service;

import com.delivery.domain.order.entity.Order;
import com.delivery.domain.order.entity.OrderStatusEnum;
//import com.delivery.domain.order.repository.OrderRepository;
import com.delivery.domain.review.dto.ReviewCreateReq;
import com.delivery.domain.review.dto.ReviewRes;
import com.delivery.domain.review.entity.Review;
import com.delivery.domain.review.repository.ReviewRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final OrderRepository orderRepository;  // TODO(#68): OrderService로 교체
    private final UserRepository userRepository;    // TODO(#68): UserService로 교체

    // 리뷰 생성
    @Override
    @Transactional
    public ReviewRes createReview(Long userId, ReviewCreateReq request) {
        log.info("[REVIEW] 생성 요청 - userId: {}, orderId: {}", userId, request.getOrderId());

        // 사용자 조회
        // TODO(#68): UserService.getUserById로 변경 (deletedAt 고려)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 주문 조회
        // TODO(#68): OrderService.getOrderById로 변경 (deletedAt 및 상태 고려)
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // 검증
        validateReviewCreation(user, order);

        // 리뷰 저장
        Review savedReview = saveReview(
                user,
                order,
                1L, // TODO(#68): order.getStore()로 교체
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

    // 리뷰 저장
    private Review saveReview(User user, Order order, Long storeId, int rating, String content) {
        Review review = Review.builder()
                .user(user)
                .storeId(storeId)    // TODO(#68): order.getStore().getId() 로 교체
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
        // TODO(#68): 실제 완료 상태 확정 시(DELIVERED/COMPLETED)로 변경
        if (order.getStatus() != OrderStatusEnum.DONE) {
            log.warn("[REVIEW] 주문 상태 불일치 - orderId: {}, status: {}, required: DELIVERED|COMPLETED",
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