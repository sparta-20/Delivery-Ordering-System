package com.delivery.store.entity;
import com.delivery.common.entity.Timestamped;
import com.delivery.store.model.StoreStatus;
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
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()", updatable = false, nullable = false)
    private UUID id;

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
    private StoreCategory category;

    // 상태값을 칼럼으로 저장하는 Enum에는 @Enumerated를 붙여서 안전하게 매핑함.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoreStatus status = StoreStatus.ACTIVE;

    // 외부 User 서비스의 사용자 PK만 보관
    @Column(nullable = false)
    private Long ownerUserId;

    // 생성 팩토리
    public static Store create(
            String name,
            StoreCategory category,
            String address,
            String city,
            String district,
            Integer minPrice,
            Long ownerUserId
    ){
        Store store = new Store();
        store.name = name;
        store.category = category;
        store.address = address;
        store.city = city;
        store.district = district;
        store.minPrice = minPrice;
        store.status = StoreStatus.ACTIVE;
        store.ownerUserId = ownerUserId;
        return store;
    }

    // 도메인 동작
    public void changeStatus(StoreStatus newStatus){
        this.status = newStatus;
    }

}
