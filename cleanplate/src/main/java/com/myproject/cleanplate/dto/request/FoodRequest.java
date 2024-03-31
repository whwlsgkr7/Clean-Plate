//package com.myproject.cleanplate.dto.request;
//
//import com.myproject.cleanplate.domain.UserAccount;
//import com.myproject.cleanplate.dto.FoodDto;
//import com.myproject.cleanplate.dto.UserAccountDto;
//
//public record FoodRequest(Integer quantity,
//                          String storage,
//                          String expiration) {
//
//    public static FoodRequest of(Integer quantity, String storage, String expiration){
//        return new FoodRequest(quantity, storage, expiration);
//    }
//
//    public FoodDto toDto(UserAccountDto userAccountDto) {
//        return FoodDto.of(
//                userAccountDto,
//                quantity,
//                storage,
//                expiration
//        )
//    }
//}
