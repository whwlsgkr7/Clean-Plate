package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Restaurant;
import com.myproject.cleanplate.domain.UserAccount;

public record RestaurantDto(String address,
//                            UserAccountDto userAccountDto,
                            String restaurantName,
                            String phoneNumber,
                            String sanitaryGrade,
                            String assignYMD,
                            String presidentName

                            ) {

    public static RestaurantDto of(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
        return new RestaurantDto(address, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }
    // open API로 음식점 목록을 저장하기 위한 생성자
//    public static RestaurantDto of(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
//        return new RestaurantDto(address, null, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
//    }

    public static RestaurantDto from(Restaurant entity){
        return new RestaurantDto(entity.getAddress(),
//                UserAccountDto.from(entity.getUserAccount()),
                entity.getRestaurantName(),
                entity.getPhoneNumber(),
                entity.getSanitaryGrade(),
                entity.getAssignYMD(),
                entity.getPresidentName());
    }

    public Restaurant toEntity() {
        return Restaurant.of(address, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }


}
