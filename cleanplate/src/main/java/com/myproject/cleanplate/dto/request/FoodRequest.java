package com.myproject.cleanplate.dto.request;

import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public record FoodRequest(String foodName,
                          Integer quantity,
                          String category,
                          String storage,
                          LocalDate expiration
                          ) {

    public static FoodRequest of(String foodName, Integer quantity, String category, String storage, LocalDate expiration){
        return new FoodRequest(foodName, quantity, category, storage, expiration);
    }

    public FoodDto toDtoUpdate(UserAccountDto userAccountDto){
        return FoodDto.of(
                userAccountDto,
                foodName,
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
