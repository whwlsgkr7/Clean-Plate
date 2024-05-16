package com.myproject.cleanplate.dto.request;

import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;

import java.util.Date;

public record FoodRequest(String foodName,
                          Integer quantity,
                          String category,
                          String storage,
                          Date expiration
                          ) {

    public static FoodRequest of(String foodName, Integer quantity, String category, String storage, Date expiration){
        return new FoodRequest(foodName, quantity, category, storage, expiration);
    }

    public FoodDto toDtoUpdate(UserAccountDto userAccountDto){
        return FoodDto.of(
                userAccountDto,
                quantity,
                storage,
                expiration
        );
    }

    public FoodDto toDtoSave(UserAccountDto userAccountDto){
        return FoodDto.of(
                foodName,
                userAccountDto,
                quantity,
                category,
                storage,
                expiration
        );
    }
}
