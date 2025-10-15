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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;

    //광화문 중심좌표/반경 설정값 주입
    @Value("${app.service-area.center-lat}")
    private double centerLat;

    @Value("${app.service-area.center-lon}")
    private double centerLon;

    @Value("${app.service-area.radius-km}")
    private double radiusKm;

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
                requestDto.getLatitude(),
                requestDto.getLongitude(),
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
                requestDto.getLatitude(),
                requestDto.getLongitude(),
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
                categoryIdLike(cond.getCategoryId()),
                withinGwangHwaMoon(centerLat, centerLon, radiusKm)
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

        // 좌표없으면 차단
        if(store.getLatitude()==null || store.getLongitude()==null){
            throw new BusinessException(ErrorCode.OUT_OF_SERVICE_AREA);
        }

        // 광화문의 위도,경도와 가게 좌표의 거리 계산
        double distKm = haversineKm(
                store.getLatitude().doubleValue(),
                store.getLongitude().doubleValue(),
                centerLat, centerLon
        );

        if(distKm>radiusKm){
            throw new BusinessException(ErrorCode.OUT_OF_SERVICE_AREA);
        }

        return new StoreRes(store);
    }

    private static BigDecimal bd(double v) {
        return BigDecimal.valueOf(v).setScale(6, RoundingMode.HALF_UP);
    }

    // 가게 목록 조회 -> 광화문 근방 바운딩 박스
    public Specification<Store> withinGwangHwaMoon(double centerLat, double centerLon, double radiusKm){

        // 반경(km)을 위도 단위로 환산
        double latDelta = radiusKm / 111.0;

        // 경도는 위도에 따른 거리 차이 때문에 cos(위도)로 보정
        double lonDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(centerLat)));

        // 중심점 기준으로 동,서,남,북 경계 좌표 계산
        BigDecimal minLat = bd(centerLat - latDelta);
        BigDecimal maxLat = bd(centerLat + latDelta);
        BigDecimal minLon = bd(centerLon - lonDelta);
        BigDecimal maxLon = bd(centerLon + lonDelta);

        return (root, query, cb) -> cb.and(
                cb.isNotNull(root.get("latitude")),
                cb.isNotNull(root.get("longitude")),
                cb.between(root.get("latitude"), minLat, maxLat),
                cb.between(root.get("longitude"), minLon, maxLon)
                );
    }

    // 가게 단건 조회 -> 광화문 근방 구면 거리
    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {

        // 지구 평균 반지름
        double R = 6371.0088;

        // 위도와 경도 차이를 radian으로 변환
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // Haversine 공식 적용
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        // 중심각 계산
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 실제 거리 반환
        return R * c;
    }
}