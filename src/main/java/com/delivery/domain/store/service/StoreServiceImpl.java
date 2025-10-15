package com.delivery.domain.store.service;

import com.delivery.domain.store.dto.StoreCreateReq;
import com.delivery.domain.store.dto.StoreRes;
import com.delivery.domain.store.dto.StoreSearchCondition;
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
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
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

        StoreCategory category = storeCategoryRepository.findByCategoryNameAndIsActiveTrue(requestDto.getCategoryName().trim()).orElseThrow(
                ()-> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Store store = new Store(
                requestDto.getName(),
                category,
                requestDto.getAddress(),
                requestDto.getCity(),
                requestDto.getDistrict(),
                requestDto.getDong(),
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

        StoreCategory category = storeCategoryRepository.findByCategoryNameAndIsActiveTrue(requestDto.getCategoryName().trim()).orElseThrow(
                ()-> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        store.update(
                requestDto.getName(),
                category,
                requestDto.getAddress(),
                requestDto.getCity(),
                requestDto.getDistrict(),
                requestDto.getDong(),
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

    // OWNER, MASTER, MANAGER - 본인 가게 조회
    @Override
    public Page<StoreRes> getMyStores(User user, Pageable pageable){
        if(user.getRole() == UserRoleEnum.CUSTOMER){
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
        }

        Page<Store> stores = storeRepository.findAllByOwnerUserIdAndStatus(user.getUserId(), StoreStatusEnum.ACTIVE, pageable);

        if(stores.isEmpty()){
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }

        return stores.map(StoreRes::from);
    }

    // MASTER, MANAGER - 점주별 가게 조회
    @Override
    public Page<StoreRes> getOwnerStores(Long ownerUserId, User user, Pageable pageable){
        if(user.getRole() != UserRoleEnum.MASTER && user.getRole() != UserRoleEnum.MANAGER){
            throw new BusinessException(ErrorCode.FORBIDDEN_READ_STORE);
        }

        Page<Store> stores = storeRepository.findAllByOwnerUserIdAndStatus(ownerUserId, StoreStatusEnum.ACTIVE,pageable);

        if(stores.isEmpty()){
            throw new BusinessException(ErrorCode.STORE_NOT_FOUND);
        }

        return stores.map(StoreRes::from);
    }

    // 가게 검색 및 목록 조회
    @Override
    public Page<StoreRes> getAllStores(StoreSearchCondition cond, Pageable pageable){

        categoryNameNeeded(cond);

        Specification<Store> spec = allOfNotNull(
                keywordLike(cond.getKeyword()),
                cityLike(cond.getCity()),
                districtLike(cond.getDistrict()),
                dongLike(cond.getDong()),
                categoryIdLike(cond.getCategoryId())
        );

        Page<Store> stores = storeRepository.findAll(spec, pageable);
        return stores.map(StoreRes::from);
    }

    @Override
    public Store getByStoreIdAndStatus(UUID storeId, StoreStatusEnum storeStatusEnum) {
        return storeRepository.findByStoreIdAndStatus(storeId, storeStatusEnum)
                .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN_READ_STORE));
    }

    // categoryName을 categoryId에 매핑
    private void categoryNameNeeded(StoreSearchCondition cond){
        if(cond.getCategoryId()!=null) return;
        if(!StringUtils.hasText(cond.getCategoryName())) return;

        StoreCategory category = storeCategoryRepository.findByCategoryNameAndIsActiveTrue(cond.getCategoryName().trim())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        cond.setCategoryId(category.getCategoryId());
    }

    // null 제외하고 AND 결합
    private static <T> Specification<T> allOfNotNull(Specification<T> ...specs){
        return Specification.allOf(
                Arrays.stream(specs)
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    // 키워드 검색
    private Specification<Store> keywordLike(String q){
        if(q==null || q.isBlank()){
            return null;
        }
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), like);
    }

    // 도시(시) 검색
    private Specification<Store> cityLike(String city){
        if(city==null || city.isBlank()){
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("city"), city);
    }

    // 세부 지역(구) 검색
    private Specification<Store> districtLike(String district){
        if(district==null || district.isBlank()){
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("district"), district);
    }

    // 동 검색
    private Specification<Store> dongLike(String dong){
        if(dong==null || dong.isBlank()){
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("dong"), dong);
    }

    // 카테고리 검색
    private Specification<Store> categoryIdLike(UUID categoryId){
        if(categoryId==null){
            return null;
        }
        return (root, query, cb) -> {
            Join<Store, StoreCategory> category = root.join("category");
            return cb.and(
                    cb.equal(category.get("categoryId"), categoryId),
                    cb.isTrue(category.get("isActive"))
            );
        };
    }

    // 가게 단건 조회
    @Override
    public StoreRes getStore(UUID storeId){
        Store store = storeRepository.findByStoreIdAndStatus(storeId, StoreStatusEnum.ACTIVE)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
        return new StoreRes(store);
    }
}