package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import com.delivery.domain.store.entity.Store;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuReq {
    @NotNull
    private UUID storeId;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String imageUrl;
    @NotNull
    private Integer price;
    @NotNull
    private Integer quantity;
    @NotNull
    private MenuStatusEnum status;

    public Menu toEntity(Store store) {
        return Menu.builder()
                .name(this.name)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .price(this.price)
                .quantity(this.quantity)
                .status(this.status)
                .store(store)
                .build();
    }
}
