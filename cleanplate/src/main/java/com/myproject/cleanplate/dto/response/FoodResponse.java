package com.myproject.cleanplate.dto.response;

import com.myproject.cleanplate.domain.Food;
import java.time.LocalDateTime;
import java.util.Date;

public record FoodResponse(Long id,
                           String foodName,
                           Integer quantity,
                           String category,
                           String storage,
                           Date expiration,
                           LocalDateTime createdAt,
                           String createdBy,
                           LocalDateTime modifiedAt,
                           String modifiedBy) {

    public static FoodResponse fromEntity(Food entity){
        return new FoodResponse(
                entity.getId(),
                entity.getFoodName(),
                entity.getQuantity(),
                entity.getCategory(),
                entity.getStorage(),
                entity.getExpiration(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getCreatedBy()
        );

    }
}
