package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuResTest {

    @Test
    @DisplayName("Menu를 이용해 생성할 수 있다.")
    void Menu를_이용해_생성할_수_있다() {
        // given
        UUID storeId = UUID.randomUUID();
        Menu menu = Menu.builder()
                .menuId(UUID.randomUUID())
                .name("name")
                .price(10000)
                .store(Store.builder().storeId(storeId).build())
                .quantity(2)
                .description("description")
                .build();

        // when
        MenuRes res = MenuRes.from(menu);

        // then
        assertThat(res.getName()).isEqualTo("name");
        assertThat(res.getPrice()).isEqualTo(10000);
        assertThat(res.getQuantity()).isEqualTo(2);
        assertThat(res.getDescription()).isEqualTo("description");
        assertThat(res.getStoreId()).isEqualTo(storeId);
    }
}