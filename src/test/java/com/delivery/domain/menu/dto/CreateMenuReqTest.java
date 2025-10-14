package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import com.delivery.domain.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateMenuReqTest {
    @Test
    @DisplayName("CreateMenuReq -> Menu 엔티티 변환")
    void toEntity_shouldMapAllFields() {
        UUID storeId = UUID.randomUUID();
        Store store = Store.builder().storeId(storeId).build();

        CreateMenuReq req = new CreateMenuReq(
                storeId,
                "치킨",
                "맛있는 치킨",
                "http://image.url/chicken.png",
                15000,
                10,
                MenuStatusEnum.AVAILABLE
        );

        Menu menu = req.toEntity(store);

        assertNotNull(menu);
        assertEquals(req.getName(), menu.getName());
        assertEquals(req.getDescription(), menu.getDescription());
        assertEquals(req.getImageUrl(), menu.getImageUrl());
        assertEquals(req.getPrice(), menu.getPrice());
        assertEquals(req.getQuantity(), menu.getQuantity());
        assertEquals(req.getStatus(), menu.getStatus());
        assertEquals(store, menu.getStore());
    }
}