package com.delivery.store.service;

import com.delivery.store.dto.StoreCreateRequestDto;
import com.delivery.store.dto.StoreResponseDto;
import com.delivery.store.entity.Store;
import com.delivery.store.entity.StoreCategory;
import com.delivery.store.repository.StoreCategoryRepository;
import com.delivery.store.repository.StoreRepository;
import com.delivery.user.entity.User;
import com.delivery.user.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class storeServiceImpl implements storeService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    // 1. OWNER, MASTER - 가게 생성
    @Override
    @Transactional
    public StoreResponseDto createStore(StoreCreateRequestDto requestDto, User user){
        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new IllegalArgumentException("고객은 가게를 생성할 수 없습니다.");
        }

        StoreCategory category = storeCategoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
                ()-> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Store store = new Store(
                requestDto.getName(),
                category,
                requestDto.getAddress(),
                requestDto.getDistrict(),
                requestDto.getCity(),
                requestDto.getMinPrice(),
                user.getUserId()
        );
        storeRepository.save(store);
        return new StroreResponseDto(store);
    }

    // 2. OWNER, MASTER - 가게 수정

    // 3. OWNER, MASTER - 가게 삭제

    // 4. 누구나 전체 가게 목록 조회

    // 5. 누구나 가게 상세 조회

    // 6. OWNER, MASTER - 본인 가게 조회


}
