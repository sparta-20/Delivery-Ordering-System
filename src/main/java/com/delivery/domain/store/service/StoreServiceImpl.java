package com.delivery.domain.store.service;

import com.delivery.domain.store.dto.StoreCreateRequestDto;
import com.delivery.domain.store.dto.StoreResponseDto;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreCategory;
import com.delivery.domain.store.repository.StoreCategoryRepository;
import com.delivery.domain.store.repository.StoreRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

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
        return new StoreResponseDto(store);
    }

    // 2. OWNER, MASTER - 가게 수정

    // 3. OWNER, MASTER - 가게 삭제

    // 4. OWNER, MASTER - 본인 가게 조회

    // 5. 누구나 전체 가게 목록 조회
    @Override
    public Page<StoreResponseDto> getStores(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction dir = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Page<Store> stores = storeRepository.findAll(pageable);
        return stores.map(StoreResponseDto::new);
    }

    // 6. 누구나 가게 상세 조회



}


