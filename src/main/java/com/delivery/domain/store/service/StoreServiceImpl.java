package com.delivery.domain.store.service;
import com.delivery.domain.store.dto.StoreCreateReq;
import com.delivery.domain.store.dto.StoreRes;
import com.delivery.domain.store.dto.StoreUpdateReq;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.entity.StoreCategory;
import com.delivery.domain.store.entity.StoreStatusEnum;
import com.delivery.domain.store.repository.StoreCategoryRepository;
import com.delivery.domain.store.repository.StoreRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.entity.UserRoleEnum;
import com.delivery.global.exception.BusinessException;
import com.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    // OWNER, MASTER, MANAGER - 가게 생성
    @Override
    @Transactional
    public StoreRes createStore(StoreCreateReq requestDto, User user){
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
                user
        );
        storeRepository.save(store);
        return new StoreRes(store);
    }

    // OWNER, MASTER, MANAGER - 가게 수정
    @Override
    @Transactional
    public StoreRes updateStore(UUID storeId, StoreUpdateReq requestDto, User user){
        Store store = storeRepository.findByStoreIdAndStatus(storeId, StoreStatusEnum.ACTIVE).orElseThrow(
                () -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new BusinessException(ErrorCode.FORBIDDEN_UPDATE_STORE);
        }
        if (!(user.getRole() == UserRoleEnum.MASTER || user.getRole() == UserRoleEnum.MANAGER ||
                (user.getRole() == UserRoleEnum.OWNER && store.getOwner().getUserId().equals(user.getUserId())))) {
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
        return new StoreRes(store);
    }

    // OWNER, MASTER - 가게 삭제
    @Override
    @Transactional
    public StoreRes deleteStore(UUID storeId, User user){
        Store store = storeRepository.findByStoreIdAndStatus(storeId, StoreStatusEnum.ACTIVE).orElseThrow(
                ()-> new BusinessException(ErrorCode.STORE_NOT_FOUND));

        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE_STORE);
        }
        if (!(user.getRole() == UserRoleEnum.MASTER || user.getRole() == UserRoleEnum.MANAGER ||
                (user.getRole() == UserRoleEnum.OWNER && store.getOwner().getUserId().equals(user.getUserId())))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_DELETE_STORE);
        }

        store.markDeleted();
        return new StoreRes(store);
    }

<<<<<<< HEAD

    // OWNER, MASTER - 본인 가게 조회
    @Override
    public Page<StoreRes> getMyStores(Long userId, Pageable pageable){
        Page<Store> stores = storeRepository.findAllByOwnerUserId(userId, pageable);
        if(stores.isEmpty()){
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }
        return stores.map(StoreRes::from);
    }
=======
    // OWNER, MASTER, MANAGER - 본인 가게 조회
    @Override
    public Page<StoreResponseDto> getMyStores(User user, Pageable pageable){
        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
        }

        Page<Store> stores = storeRepository.findAllByOwnerUserIdAndStatus(user.getUserId(), StoreStatusEnum.ACTIVE, pageable);

        if(stores.isEmpty()){
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }

        return stores.map(StoreResponseDto::from);
    }

    // MASTER, MANAGER - 점주별 가게 조회
    @Override
    public Page<StoreResponseDto> getOwnerStores(Long ownerUserId, User user, Pageable pageable){
        if(user.getRole() != UserRoleEnum.MASTER && user.getRole() != UserRoleEnum.MANAGER){
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
        }

        Page<Store> stores = storeRepository.findAllByOwnerUserIdAndStatus(ownerUserId, StoreStatusEnum.ACTIVE,pageable);

        if(stores.isEmpty()){
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }

        return stores.map(StoreResponseDto::from);
    }

    // 가게 검색 + 전체 가게 목록 조회



    // 가게 상세 조회

    // 가게 상세 수정


>>>>>>> 6d94dfa (MANAGER 조회 권한 추가)
}


