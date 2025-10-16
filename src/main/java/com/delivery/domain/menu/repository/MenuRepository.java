package com.delivery.domain.menu.repository;

import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {
    Optional<Menu> findByMenuIdAndDeletedAtIsNull(UUID menuId);

    @Query("SELECT new com.delivery.domain.menu.dto.MenuRes(" +
            "m.menuId, m.store.storeId, m.name, m.description, m.imageUrl, " +
            "m.price, m.quantity, m.status, m.createdAt) " +
            "FROM Menu m " +
            "WHERE m.store.storeId = :storeId " +
            "AND m.status IN :status")
    Page<MenuRes> findByStoreIdAndStatus(
            @Param("storeId") UUID storeId,
            @Param("status") List<MenuStatusEnum> status,
            Pageable pageable);


}