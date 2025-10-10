package com.delivery.domain.store.service;

import com.delivery.domain.store.dto.StoreCreateRequestDto;
import com.delivery.domain.store.dto.StoreResponseDto;
import com.delivery.domain.store.dto.StoreUpdateRequestDto;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreCategory;
import com.delivery.domain.store.repository.StoreCategoryRepository;
import com.delivery.domain.store.repository.StoreRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    // OWNER, MASTER - 가게 생성
    @Override
    @Transactional
    public StoreResponseDto createStore(StoreCreateRequestDto requestDto, User user){
        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new BusinessException(ErrorCode.FORBIDDEN_CREATE_STORE);
        }

        StoreCategory category = storeCategoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
                ()-> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Store store = new Store(
                requestDto.getName(),
                category,
                requestDto.getAddress(),
                requestDto.getCity(),
                requestDto.getDistrict(),
                requestDto.getMinPrice(),
                user.getUserId()
        );
        storeRepository.save(store);
        return new StoreResponseDto(store);
    }

    // OWNER, MASTER - 가게 수정
    @Override
    @Transactional
    public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto requestDto, User user){
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new BusinessException(ErrorCode.FORBIDDEN_UPDATE_STORE);
        }
        if(user.getRole() == UserRoleEnum.MASTER && !store.getOwnerUserId().equals(user.getUserId())){
            throw new BusinessException(ErrorCode.FORBIDDEN_UPDATE_STORE);
        }

        StoreCategory category = storeCategoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
                ()-> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        store.update(
                requestDto.getName(),
                category,
                requestDto.getAddress(),
                requestDto.getCity(),
                requestDto.getDistrict(),
                requestDto.getMinPrice(),
                requestDto.getStatus()
        );
        return new StoreResponseDto(store);
    }

    // OWNER, MASTER - 가게 삭제
    @Override
    @Transactional
    public StoreResponseDto deleteStore(UUID storeId, User user){
        Store store = storeRepository.findById(storeId).orElseThrow(
                ()-> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE_STORE);
        }
        if(user.getRole() == UserRoleEnum.MASTER && !store.getOwnerUserId().equals(user.getUserId())){
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE_STORE);
        }
        storeRepository.delete(store);
        return new StoreResponseDto(store);
    }

    // OWNER, MASTER - 본인 가게 조회


    // MASTER - 점주별 가게 조회


    // 전체 가게 목록 조회


    // 가게 상세 조회


    // 지역별 가게 조회


    // 카테고리별 가게 조회



}


