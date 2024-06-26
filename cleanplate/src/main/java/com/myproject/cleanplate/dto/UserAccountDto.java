package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.Restaurant;
import com.myproject.cleanplate.domain.UserAccount;

import java.time.LocalDateTime;
import java.util.List;

public record UserAccountDto(String username,
                             List<Food> foodList,
                             String password,
                             String role,
                             String nickName,
                             String email,
                             String address,
                             LocalDateTime createdAt,
                             String createdBy,
                             LocalDateTime modifiedAt,
                             String modifiedBy) {

    public static UserAccountDto of(String username,
                                    List<Food> foodList,
                                    String password,
                                    String role,
                                    String nickName,
                                    String email,
                                    String address,
                                    LocalDateTime createdAt,
                                    String createdBy,
                                    LocalDateTime modifiedAt,
                                    String modifiedBy){
        return new UserAccountDto(username, foodList, password, role, nickName, email, address, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto of(String username,
                                    String password,
                                    String nickName,
                                    String email,
                                    String address
                                    ){
        return new UserAccountDto(username, null, password, null, nickName, email, address, null, null, null, null);
    }



    public static UserAccountDto from(UserAccount entity){
        return new UserAccountDto(entity.getUsername(),
                entity.getFoodList(),
                entity.getPassword(),
                entity.getRole(),
                entity.getNickName(),
                entity.getEmail(),
                entity.getAddress(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy());
    }

    public UserAccount toEntity(){
        return UserAccount.of(username,
                password,
                role,
                nickName,
                email,
                address
                );
    }
}
