package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import com.delivery.domain.store.entity.Store;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuReq {
    @NotEmpty
    private UUID storeId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private String imageUrl;
    @NotEmpty
    private Integer price;
    @NotEmpty
    private Integer quantity;
    @NotEmpty
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
