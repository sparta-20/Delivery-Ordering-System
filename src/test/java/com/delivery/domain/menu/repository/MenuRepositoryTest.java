package com.delivery.domain.menu.repository;

import com.delivery.domain.menu.dto.MenuRes;
import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import com.delivery.domain.store.entity.Store;
import com.delivery.domain.store.category.entity.StoreCategory;
import com.delivery.domain.store.category.repository.StoreCategoryRepository;
import com.delivery.domain.store.repository.StoreRepository;
import com.delivery.domain.user.entity.User;
import com.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class MenuRepositoryTest {

    @Autowired private MenuRepository menuRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StoreCategoryRepository storeCategoryRepository;

    @Test
    @DisplayName("findByStoreId JPQL DTO 정상동작")
    void testFindByStoreId() {

        //given
        User owner = createUser();
        StoreCategory storeCategory = createCategory();
        Store store = createStore(owner, storeCategory);
        Menu menu1 = createMenu(store, "Pizza", MenuStatusEnum.AVAILABLE);
        createMenu(store, "Chicken", MenuStatusEnum.HIDDEN);

        // when
        Page<MenuRes> result = menuRepository.findByStoreIdAndStatus(store.getStoreId(), List.of(MenuStatusEnum.AVAILABLE), PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getMenuId()).isEqualTo(menu1.getMenuId());
        assertThat(result.getContent().getFirst().getPrice()).isEqualTo(menu1.getPrice());
        assertThat(result.getContent().getFirst().getQuantity()).isEqualTo(menu1.getQuantity());
        assertThat(result.getContent().getFirst().getStatus()).isEqualTo(menu1.getStatus());
        assertThat(result.getContent().getFirst().getImageUrl()).isEqualTo(menu1.getImageUrl());
        assertThat(result.getContent().getFirst().getName()).isEqualTo(menu1.getName());
        assertThat(result.getContent().getFirst().getCreatedAt()).isEqualTo(menu1.getCreatedAt());
    }

    private Menu createMenu(Store store, String menuName, MenuStatusEnum status) {
        Menu menu = new Menu(menuName, "image1.png", "Delicious burger", 5000, 10, status, store);
        menuRepository.save(menu);
        return menu;
    }

    private StoreCategory createCategory() {
        StoreCategory storeCategory = new StoreCategory("카테고리 이름");
        storeCategoryRepository.save(storeCategory);
        return storeCategory;
    }

    private User createUser() {
        User owner = new User("nickname", "email@email.com", "encodedPassword", "01012341234");
        userRepository.save(owner);
        return owner;
    }

    private Store createStore(User owner, StoreCategory storeCategory){
        Store store = new Store(
                "가게이름",
                storeCategory,
                "address",
                "city",
                "district",
                "dong",
                BigDecimal.ONE,
                BigDecimal.ONE,
                10000,
                owner
        );
        storeRepository.save(store);
        return store;
    }
}
