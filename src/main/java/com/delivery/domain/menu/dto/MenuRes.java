package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MenuRes {
    private UUID menuId;
    private UUID storeId;
    private String name;
    private String description;
    private String imageUrl;
    private Integer price;
    private Integer quantity;
    private MenuStatusEnum status;
    private LocalDateTime createdAt;

    public static MenuRes from(Menu menu) {
        return new MenuRes(
                menu.getMenuId(),
                menu.getStore().getStoreId(),
                menu.getName(),
                menu.getDescription(),
                menu.getImageUrl(),
                menu.getPrice(),
                menu.getQuantity(),
                menu.getStatus(),
                menu.getCreatedAt()
        );
    }
}
