package com.myproject.cleanplate.dto.response;

import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.UserAccountDto;

public record UserJoinResponse(String username,
                               String role,
                               String nickName,
                               String email,
                               String address
) {
    public static UserJoinResponse of(String username,
                                      String role,
                                      String nickName,
                                      String email,
                                      String address){
        return new UserJoinResponse(username, role, nickName, email, address);
    }

    public static UserJoinResponse fromEntity(UserAccount entity){
        return new UserJoinResponse(entity.getUsername(), entity.getRole(), entity.getNickName(), entity.getEmail(), entity.getAddress());
    }

}
