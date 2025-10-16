package com.delivery.domain.menu.dto;

import com.delivery.domain.menu.entity.MenuStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMenuReq {
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
}
