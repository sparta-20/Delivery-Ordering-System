package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.Menu;
import com.delivery.domain.menu.entity.MenuStatus;
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
    private Integer price;
    private Integer quantity;
    private MenuStatus status;
    private LocalDateTime createdAt;

    public static MenuRes from(Menu menu) {
        return new MenuRes(
                menu.getMenuId(),
                menu.getStore().getStoreId(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.getQuantity(),
                menu.getStatus(),
                menu.getCreatedAt()
        );
    }
}
