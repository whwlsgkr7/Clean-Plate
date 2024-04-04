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

    public static UserJoinResponse from(UserAccount userAccount){
        return new UserJoinResponse(userAccount.getUsername(), userAccount.getRole(), userAccount.getNickName(), userAccount.getEmail(), userAccount.getAddress());
    }

}
