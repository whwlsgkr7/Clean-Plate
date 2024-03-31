package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.Restaurant;
import com.myproject.cleanplate.domain.UserAccount;

import java.time.LocalDateTime;
import java.util.List;

public record UserAccountDto(String userId,
                             List<Food> foodList,
                             List<Restaurant> restaurantList,
                             String pwd,
                             String nickName,
                             String email,
                             String address,
                             LocalDateTime createdAt,
                             String createdBy,
                             LocalDateTime modifiedAt,
                             String modifiedBy) {

    public static UserAccountDto of(String userId,
                                    List<Food> foodList,
                                    List<Restaurant> restaurantList,
                                    String pwd,
                                    String nickName,
                                    String email,
                                    String address,
                                    LocalDateTime createdAt,
                                    String createdBy,
                                    LocalDateTime modifiedAt,
                                    String modifiedBy){
        return new UserAccountDto(userId, foodList, restaurantList, pwd, nickName, email, address, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto from(UserAccount entity){
        return new UserAccountDto(entity.getUserId(),
                entity.getFoodList(),
                entity.getRestaurantList(),
                entity.getPwd(),
                entity.getNickName(),
                entity.getEmail(),
                entity.getAddress(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy());
    }

    public UserAccount toEntity(){
        return UserAccount.of(userId,
                pwd,
                nickName,
                email,
                address
                );
    }
}
