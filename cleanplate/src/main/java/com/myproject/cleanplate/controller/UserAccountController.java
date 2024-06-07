package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.dto.AlarmResponse;
import com.myproject.cleanplate.dto.CustomUserDetails;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.dto.response.UserJoinResponse;

import com.myproject.cleanplate.service.AlarmService;
import com.myproject.cleanplate.service.TokenService;
import com.myproject.cleanplate.service.UserAccountService;
import com.myproject.cleanplate.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;
    //    private final AlarmService alarmService;
    private final AlarmService alarmService;
    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    private final TokenService tokenService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserAccountDto userAccountDto){
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
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userJoinResponse, HttpStatus.OK) ;
    }


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return tokenService.reissueToken(request, response);
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
        SseEmitter sseEmitter = alarmService.subscribe(username);

        alarmService.notifyAllUsersExpirationAndRecipe();

        return sseEmitter;

    }

    @GetMapping("alarmList")
    public ResponseEntity<?> alarmList(@AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails.getUsername();
        try {
            return ResponseEntity.ok(userAccountService.alarmList(username));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("alarm/deleteAll")
    public ResponseEntity<?> alarmDeleteAll(){
        try {
            alarmService.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }

        return ResponseEntity.ok("success");
    }



//    @GetMapping("/alarm2/subscribe")
//    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(required = false) String token) {
//        String username = userDetails.getUsername();
//        System.out.println("Received token: " + token); // 토큰 값 로그 출력
//
//        SseEmitter sseEmitter = notificationService.subscribe(username);
//        notificationService.notifyAllUsersExpiration();
//        return sseEmitter;
//    }




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
