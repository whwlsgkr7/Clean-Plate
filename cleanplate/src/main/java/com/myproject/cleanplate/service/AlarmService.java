//package com.myproject.cleanplate.service;
//
//import com.myproject.cleanplate.repository.EmitterRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AlarmService {
//    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
//    private final static String ALARM_NAME = "alarm";
//    private final EmitterRepository emitterRepository;
//
//    public void send(Integer alarmId, String username, String foodName) throws Exception{
//        // ifPresentOrElse는 값이 존재하면 첫 번째 람다 함수를 실행하고, 값이 없을 경우 두 번째 람다 함수를 실행
//        // 여기서는 두 번째 함수가 누락되어 있으며, 값이 없는 경우 아무 일도 일어나지 않는다.
//        emitterRepository.get(username).ifPresentOrElse(sseEmitter -> {
//            try {
//                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new alarm"));
//            } catch (IOException e) {
//                emitterRepository.delete(username);
//                throw new RuntimeException(e);
//            }
//        },
//                () -> log.info("No emitter founded")
//        );
//    }
//
//
//    public SseEmitter connectAlarm(String username){
//        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
//
//        sseEmitter.onCompletion(() -> emitterRepository.delete(username));
//        sseEmitter.onTimeout(()-> emitterRepository.delete(username));
//
//        try {
//            sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect completed"));
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        return sseEmitter;
//    }
//}
