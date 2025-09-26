package com.delivery.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestEntityRepository repository;


    @Transactional
    public Long save(TestEntity entity) {
        return repository.save(entity).getId();
    }

    public TestEntity getTestEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NOT EXIST ENTITY"));
    }
}
