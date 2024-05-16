package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.UserAccount;

import java.time.LocalDateTime;
import java.util.Date;

public record FoodDto(Long foodId,
                      String foodName,
                      UserAccountDto userAccountDto,
                      Integer quantity,
                      String category,
                      String storage,
                      Date expiration,
                      LocalDateTime createdAt,
                      String createdBy,
                      LocalDateTime modifiedAt,
                      String modifiedBy) {
    public static FoodDto of(Long foodId,
                             String foodName,
                                   UserAccountDto userAccountDto,
                                   Integer quantity,
                                   String category,
                                   String storage,
                                   Date expiration,
                                   LocalDateTime createdAt,
                                   String createdBy,
                                   LocalDateTime modifiedAt,
                                   String modifiedBy){
        return new FoodDto(foodId, foodName, userAccountDto, quantity, category, storage, expiration, createdAt, createdBy, modifiedAt, modifiedBy);
    }


    // 음식 저장 용도
    public static FoodDto of(String foodName,
                             UserAccountDto userAccountDto,
                             Integer quantity,
                             String category,
                             String storage,
                             Date expiration
    ){
        return new FoodDto(null, foodName, userAccountDto, quantity, category, storage, expiration, null, null, null, null);
    }


    // 보관 음식 수정 용도
    public static FoodDto of(UserAccountDto userAccountDto,
                             Integer quantity,
                             String storage,
                             Date expiration
                             ){
        return new FoodDto(null, null, userAccountDto, quantity, null, storage, expiration, null, null, null, null);
    }
    public static FoodDto from(Food entity) {
        return new FoodDto(
                entity.getId(),
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
