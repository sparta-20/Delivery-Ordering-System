package com.delivery.domain.store.entity;

import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_store_category")
public class StoreCategory extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private boolean isActive = true;

    public StoreCategory(String code, String name) {
        this.name = name;
        this.isActive = true;
    }

    public void activate()   { this.isActive = true; }
    public void deactivate() { this.isActive = false; }
}

