package com.delivery.domain.store.category.entity;

import com.delivery.global.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_store_category")
public class StoreCategory extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID categoryId;

    @Column(nullable = false, length = 50)
    private String categoryName;

    @Column(nullable = false)
    private boolean isActive = true;

    public StoreCategory(String name) {
        this.categoryName = name;
    }

    public void activate()   { this.isActive = true; }
    public void deactivate() { this.isActive = false; }
}