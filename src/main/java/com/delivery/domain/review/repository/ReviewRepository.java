package com.delivery.domain.review.repository;

import com.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    // 주문별 리뷰 존재 여부 확인
    boolean existsByOrder_OrderIdAndDeletedAtIsNull(UUID orderId);
}