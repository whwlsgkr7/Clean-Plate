package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.CustomUserDetails;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.dto.response.UserJoinResponse;

import com.myproject.cleanplate.service.NotificationService;
import com.myproject.cleanplate.service.UserAccountService;
import com.myproject.cleanplate.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;
//    private final AlarmService alarmService;
    private final NotificationService notificationService;
    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

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


//    @GetMapping("/alarm2/subscribe/{username}")
//    public SseEmitter subscribe(@PathVariable String username) {
//        SseEmitter sseEmitter = notificationService.subscribe(username);
//
//        notificationService.notifyAllUsersExpiration();
//
//        return sseEmitter;
//
//    }



    // 메시지 알림
    @GetMapping("/alarm2/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        SseEmitter sseEmitter = notificationService.subscribe(username);

        notificationService.notifyExpiration(username);

        return sseEmitter;

    }



// Authentication 을 매개변수로 선언하는 이유는 자바스크립트의 EventSource는 헤더 부분을 설정하는 것을 지원하지 않기 때문에 path에 토큰을 포함시켜서 서버로 전달해야한다.
    // 때문에 subscribe 하기전에 토큰이 유효한지 수동으로 체크하기 위해 Authentication을 받아오는 것이다.
//    @GetMapping("/alarm/subscribe")
//    public SseEmitter subscribe(Authentication authentication){
//        // Authentication 객체에서 UserAccountDto 인스턴스 추출
//        UserAccountDto user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserAccountDto.class);
//        if (user == null) {
//            // user가 null일 경우, 적절한 예외 처리 또는 에러 응답
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
//        }
//        return alarmService.connectAlarm(user.username());
//    }



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
