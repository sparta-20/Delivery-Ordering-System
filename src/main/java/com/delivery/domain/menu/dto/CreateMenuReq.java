package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.MenuStatus;
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
    private MenuStatus status;
}
