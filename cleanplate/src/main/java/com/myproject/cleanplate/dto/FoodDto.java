package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.UserAccount;

import java.time.LocalDateTime;
import java.util.Date;

public record FoodDto(String foodName,
                      UserAccountDto userAccountDto,
                      Integer quantity,
                      String category,
                      String storage,
                      Date expiration,
                      LocalDateTime createdAt,
                      String createdBy,
                      LocalDateTime modifiedAt,
                      String modifiedBy) {
    public static FoodDto of(String foodName,
                                   UserAccountDto userAccountDto,
                                   Integer quantity,
                                   String category,
                                   String storage,
                                   Date expiration,
                                   LocalDateTime createdAt,
                                   String createdBy,
                                   LocalDateTime modifiedAt,
                                   String modifiedBy){
        return new FoodDto(foodName, userAccountDto, quantity, category, storage, expiration, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static FoodDto from(Food entity) {
        return new FoodDto(
                entity.getFoodName(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getQuantity(),
                entity.getCategory(),
                entity.getStorage(),
                entity.getExpiration(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }



    public Food toEntity(UserAccount userAccount){
        return Food.of(foodName, userAccount, quantity, category, storage, expiration);
    }
}
