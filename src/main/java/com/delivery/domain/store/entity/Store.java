package com.delivery.domain.store.entity;
import com.delivery.domain.store.model.StoreStatusEnum;
import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_stores")
public class Store extends Timestamped {

    // PK를 자동생성: DB생성 방식
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID storeId;

    @Column(nullable = false, length=100)
    private String name;

    @Column(nullable = false, length=200)
    private String address;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String district;

    @Column(nullable = false)
    private Integer minPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private StoreCategory categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoreStatusEnum status = StoreStatusEnum.ACTIVE;

    public void changeStatus(StoreStatusEnum newStatus) {
        this.status = newStatus;
    }

    public void markDeleted() {
        this.status = StoreStatusEnum.INACTIVE;
    }

    // 외부 User 서비스의 사용자 PK만 보관
    @Column(nullable = false)
    private Long ownerUserId;

    // 가게 생성 생성자
    public Store (
            String name,
            StoreCategory categoryId,
            String address,
            String city,
            String district,
            Integer minPrice,
            Long ownerUserId
    ){
        this.name = name;
        this.categoryId = categoryId;
        this.address = address;
        this.city = city;
        this.district = district;
        this.minPrice = minPrice;
        this.ownerUserId = ownerUserId;
        this.status = StoreStatusEnum.ACTIVE;
    }

    // 가게 수정 메서드
    public void update(String name,
                       StoreCategory categoryId,
                       String address,
                       String city,
                       String district,
                       Integer minPrice,
                       StoreStatusEnum status) {
        this.name = name;
        this.categoryId = categoryId;
        this.address = address;
        this.city = city;
        this.district = district;
        this.minPrice = minPrice;
        this.status = status;
    }

}
