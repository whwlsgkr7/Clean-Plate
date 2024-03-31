package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Restaurant;
import com.myproject.cleanplate.domain.UserAccount;

public record RestaurantDto(String address,
                            UserAccountDto userAccountDto,
                            String restaurantName,
                            String phoneNumber,
                            String sanitaryGrade,
                            String assignYMD,
                            String presidentName

                            ) {

    public static RestaurantDto of(String address, UserAccountDto userAccountDto, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
        return new RestaurantDto(address, userAccountDto, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }
    // open API로 음식점 목록을 저장하기 위한 생성자
    public static RestaurantDto of(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
        return new RestaurantDto(address, null, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }

    public Restaurant toEntity() {
        return Restaurant.of(address, null, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }


}
