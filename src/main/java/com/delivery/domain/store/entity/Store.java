package com.delivery.domain.store.entity;
//import com.delivery.domain.menu.entity.Menu;
//import com.delivery.domain.order.entity.Order;
//import com.delivery.domain.review.entity.Review;
import com.delivery.domain.user.entity.User;
import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import java.util.List;
//import java.util.ArrayList;
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
    private StoreCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoreStatusEnum status = StoreStatusEnum.ACTIVE;

    public void changeStatus(StoreStatusEnum newStatus) {
        this.status = newStatus;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;


    public void markDeleted() {
        this.status = StoreStatusEnum.INACTIVE;
    }

    // 가게 생성 생성자
    public Store (
            String name,
            StoreCategory category,
            String address,
            String city,
            String district,
            Integer minPrice,
            User owner
    ){
        this.name = name;
        this.category = category;
        this.address = address;
        this.city = city;
        this.district = district;
        this.minPrice = minPrice;
        this.owner = owner;
        this.status = StoreStatusEnum.ACTIVE;
    }

    // 가게 수정 메서드
    public void update(String name,
                       StoreCategory category,
                       String address,
                       String city,
                       String district,
                       Integer minPrice,
                       StoreStatusEnum status) {
        if (name != null) this.name = name;
        if (category != null) this.category = category;
        if (address != null) this.address = address;
        if (city != null) this.city = city;
        if (district != null) this.district = district;
        if (minPrice != null) this.minPrice = minPrice;
        if (status != null) this.status = status;
    }

}
