package com.delivery.domain.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
class TestEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String value;

    private String isNullField = null;

    public TestEntity(String value) {
        this.value = value;
    }
}