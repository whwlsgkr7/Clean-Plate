package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Restaurant;
import com.myproject.cleanplate.domain.UserAccount;

public record RestaurantDto(String address,
                            UserAccount userAccount,
                            String restaurantName,
                            String phoneNumber,
                            String sanitaryGrade,
                            String assignYMD,
                            String presidentName

                            ) {

    public static RestaurantDto of(String address, UserAccount userAccount, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
        return new RestaurantDto(address, userAccount, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }

    public static RestaurantDto of(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
        return new RestaurantDto(address, null, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }

    public Restaurant toEntity(RestaurantDto dto) {
        return Restaurant.of(dto.address(), dto.userAccount(), dto.restaurantName(), dto.phoneNumber(), dto.sanitaryGrade(), dto.assignYMD(), dto.presidentName());
    }


}
