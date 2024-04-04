package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.dto.response.UserJoinResponse;
import com.myproject.cleanplate.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountController {
    @Autowired
    private final UserAccountService userAccountService;

    @PostMapping("/join")
    public UserJoinResponse join(@RequestBody UserAccountDto userAccountDto){
        UserJoinResponse userJoinResponse = null;
        System.out.println(userAccountDto.password());
        try {
            userJoinResponse = userAccountService.join(userAccountDto.username(),
                    userAccountDto.password(),
                    userAccountDto.role(),
                    userAccountDto.nickName(),
                    userAccountDto.email(),
                    userAccountDto.address());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userJoinResponse;
    }

//    @PostMapping("/login")
//    public UserLoginResponse login(@RequestBody UserAccountDto dto){
//        String token = null;
//        try {
//            token = userAccountService.login(dto.username(), dto.password());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new UserLoginResponse(token);
//    }
}
