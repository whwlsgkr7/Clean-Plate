//package com.myproject.cleanplate.repository;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Slf4j
//@Repository
//public class EmitterRepository {
//    private Map<String, SseEmitter> emitterMap = new HashMap<>();
//
//    public SseEmitter save(String username, String foodname, SseEmitter sseEmitter){
//        final String key = getkey(username);
//        emitterMap.put(key, sseEmitter);
//        // 어떤 사용자의 SseEmitter가 설정되었는지 정보를 기록
//        log.info("Set sseEmitter for {}", username);
//        return sseEmitter;
//    }
//
//    public Optional<SseEmitter> get(String username){
//        final String key = getkey(username);
//        log.info("Set sseEmitter {}", username);
//        // 맵에서 검색된 SseEmitter 객체를 Optional로 감싸 반환
//        return Optional.ofNullable(emitterMap.get(key));
//
//    }
//    // 사용자 이름을 받아 해당 사용자의 SseEmitter를 맵에서 제거
//    public void delete(String username){
//        emitterMap.remove(getkey(username));
//    }
//
//    private String getkey(String username){
//        return "Emitter:UID:" + username;
//    }
//}
