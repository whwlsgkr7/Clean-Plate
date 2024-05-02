package com.myproject.cleanplate.service;

import com.myproject.cleanplate.controller.UserAccountController;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.repository.FoodRepository;
import com.myproject.cleanplate.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FoodRepository foodRepository;
    private final UserAccountRepository userRepository;

    // 메시지 알림
    public SseEmitter subscribe(String username) {

        // 1. 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 2. 연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. 저장
        UserAccountController.sseEmitters.put(username, sseEmitter);

        // 4. 연결 종료 처리
        sseEmitter.onCompletion(() -> UserAccountController.sseEmitters.remove(username));	// sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> UserAccountController.sseEmitters.remove(username));		// sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onError((e) -> UserAccountController.sseEmitters.remove(username));		// sseEmitter 연결에 오류가 발생할 경우

        return sseEmitter;
    }




    // 모든 사용자에 대한 소비기한 알림
    // @Scheduled 는 반환 타입이 void이고 매개변수가 없는 메서드에만 사용할 수 있다.
    @Transactional(readOnly = true)
    @Scheduled(fixedRate = 1000) // 예: 10초마다 실행
    public void notifyAllUsersExpiration() {
        List<UserAccount> users = userRepository.findAll(); // 모든 사용자 가져오기
        for (UserAccount user : users) {
            notifyExpiration(user.getUsername()); // 각 사용자에 대해 알림
        }
    }

    // 단일 사용자에 대한 소비기한 알림
    public void notifyExpiration(String receiver) {
        UserAccount user = userRepository.findByUsername(receiver);
        if (user == null) return;
        String username = user.getUsername();
        List<FoodDto> expiringFoods = foodRepository.findByExpirationWithinThreeDays()
                .stream()
                .map(FoodDto::from)
                .filter(food -> food.userAccountDto().username().equals(username))
                .collect(Collectors.toList());

        if (UserAccountController.sseEmitters.containsKey(username)) {
            SseEmitter sseEmitterReceiver = UserAccountController.sseEmitters.get(username);
            try {
                for (FoodDto food : expiringFoods) {
                    String message = "주의: " + food.foodName() + "의 유통기한이 " + food.expiration() + "까지입니다.";
                    sseEmitterReceiver.send(SseEmitter.event().name("expirationAlert").data(message));
                }
            } catch (Exception e) {
                UserAccountController.sseEmitters.remove(username);
            }
        }
    }




    // 소비기한 알림 - receiver 에게
//    @Transactional(readOnly = true)
//    @Scheduled(fixedRate = 10000)
//    public void notifyExpiration(String receiver) {
//        // 5. 수신자 정보 조회
//        UserAccount user = userRepository.findByUsername(receiver);
//
//        // 6. 수신자 정보로부터 id 값 추출
//        String username = user.getUsername();
//
//        // 특정 사용자의 유통기한이 3일 이내인 식품만 가져옴
//        List<FoodDto> expiringFoods = foodRepository.findByExpirationWithinThreeDays()
//                .stream()
//                .map(FoodDto::from)
//                .filter(food -> food.userAccountDto().username().equals(username)) // 특정 사용자 필터
//                .collect(Collectors.toList());
//
//        // 사용자의 SSE Emitter가 있는지 확인
//        if (UserAccountController.sseEmitters.containsKey(user.getUsername())) {
//            SseEmitter sseEmitterReceiver = UserAccountController.sseEmitters.get(username);
//
//            // 유통기한이 임박한 식품에 대한 알림 메시지 전송
//            try {
//                for (FoodDto food : expiringFoods) {
//                    String message = "주의: " + food.foodName() + "의 유통기한이 " + food.expiration() + "까지입니다.";
//                    sseEmitterReceiver.send(SseEmitter.event().name("expirationAlert").data(message));
//                }
//            } catch (Exception e) {
//                // 에러 발생 시 SSE Emitter 제거
//                UserAccountController.sseEmitters.remove(user.getUsername());
//            }
//        }
//    }

    // 댓글 알림 - 게시글 작성자 에게
//    public void notifyComment(Long postId) {
//        Post post = postRepository.findById(postId).orElseThrow(
//                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
//        );
//
//        Long userId = post.getUser().getId();
//
//        if (NotificationController.sseEmitters.containsKey(userId)) {
//            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
//            try {
//                sseEmitter.send(SseEmitter.event().name("addComment").data("댓글이 달렸습니다."));
//            } catch (Exception e) {
//                NotificationController.sseEmitters.remove(userId);
//            }
//        }
//    }
}
