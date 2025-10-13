package com.delivery.domain.review.entity;

import com.delivery.domain.order.entity.Order;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_review")
@Getter
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id", nullable = false)
    private UUID reviewId;

    // 작성자 (N:1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // 필요할 때만 로딩
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 가게 (N:1) -> Store 엔티티 생성 전까지 UUID로 보관
    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    // 주문 (1:1) — 주문당 리뷰 1건
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    // 평점 (1~5)
    @Column(name = "rating", nullable = false)
    private int rating;

    // 리뷰 내용
    @Column(name = "content", length = 500)
    private String content;

    @Builder
    private Review(User user, UUID storeId, Order order, int rating, String content) {
        this.user = user;
        this.storeId = storeId;
        this.order = order;
        this.rating = rating;
        this.content = content;
    }
}