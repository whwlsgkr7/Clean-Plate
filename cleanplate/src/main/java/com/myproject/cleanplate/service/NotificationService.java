package com.myproject.cleanplate.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.cleanplate.controller.UserAccountController;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.CompletionDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FoodRepository foodRepository;
    private final UserAccountRepository userRepository;
    private final ChatGPTService chatGPTService;


    public SseEmitter subscribe(String username) {

        // 1. SSE 연결의 타임아웃 시간을 설정
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 2. 연결 확인 이벤트 전송
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. sseEmitters Map에 유저 이름과 해당 유저의 Emitter 정보 저장
        UserAccountController.sseEmitters.put(username, sseEmitter);

        // 4. 연결 종료 처리
        sseEmitter.onCompletion(() -> UserAccountController.sseEmitters.remove(username));	// sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> UserAccountController.sseEmitters.remove(username));		// sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onError((e) -> UserAccountController.sseEmitters.remove(username));		// sseEmitter 연결에 오류가 발생할 경우

        return sseEmitter;
    }




    // 모든 사용자를 한 명씩 알림 대상이 되는지 검증
    // @Scheduled 는 반환 타입이 void이고 매개변수가 없는 메서드에만 사용할 수 있다.
    @Transactional(readOnly = true)
    @Scheduled(fixedRate = 10000) // 예: 10초마다 실행
    public void notifyAllUsersExpiration() {
        List<UserAccount> users = userRepository.findAll(); // 모든 사용자 가져오기
        for (UserAccount user : users) {
            notifyExpiration(user.getUsername()); // 각 사용자에 대해 알림
            notifyRecipe(user.getUsername());
        }
    }

    // sseEmitters Map에 등록된 사용자인지 검증 후 소비기한이 3일이내인 경우 알림
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



    public void notifyRecipe(String receiver) {
        UserAccount user = userRepository.findByUsername(receiver);
        if (user == null) return;
        String username = user.getUsername();
        List<FoodDto> expiringFoods = foodRepository.findByExpirationWithinThreeDays()
                .stream()
                .map(FoodDto::from)
                .filter(food -> food.userAccountDto().username().equals(username))
                .collect(Collectors.toList());

        if (!expiringFoods.isEmpty()) {
            String prompt = createPrompt(expiringFoods);
            CompletionDto completionDto = new CompletionDto("gpt-3.5-turbo-instruct", prompt, 0, 1000);
            Map<String, Object> recipeResponse = chatGPTService.legacyPrompt(completionDto);

            String recipeMessage = "Recipe suggestions could not be retrieved.";
            if (recipeResponse.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) recipeResponse.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    recipeMessage = (String) choices.get(0).get("text");
                }
            }

            String jsonRecipe = formatRecipeAsJson(recipeMessage); // JSON으로 변환

            if (UserAccountController.sseEmitters.containsKey(username)) {
                SseEmitter sseEmitterReceiver = UserAccountController.sseEmitters.get(username);
                try {
                    sseEmitterReceiver.send(SseEmitter.event().name("recipeSuggestion").data(jsonRecipe));
                } catch (Exception e) {
                    UserAccountController.sseEmitters.remove(username);
                }
            }
        }
    }


    private String formatRecipeAsJson(String recipe) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> recipeMap = new HashMap<>();
        recipeMap.put("recipe", recipe);

        String jsonResult = "{}";
        try {
            jsonResult = mapper.writeValueAsString(recipeMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }


    private String createPrompt(List<FoodDto> expiringFoods) {
        String ingredientList = expiringFoods.stream()
                .map(FoodDto::foodName)
                .collect(Collectors.joining(", "));
        return "당신은 이제부터 세계 최고의 요리사입니다." + ingredientList + "를 이용하여 집에서 준비할 수 있는 간단한 레시피를 제안해주세요." +
                "요리 이름, 재료, 요리 설명 순서대로 언급해주세요.";
    }


//    public void notifyRecipe(String receiver) {
//        UserAccount user = userRepository.findByUsername(receiver);
//        if (user == null) return;
//        String username = user.getUsername();
//        List<FoodDto> expiringFoods = foodRepository.findByExpirationWithinThreeDays()
//                .stream()
//                .map(FoodDto::from)
//                .filter(food -> food.userAccountDto().username().equals(username))
//                .collect(Collectors.toList());
//
//        if (!expiringFoods.isEmpty()) {
//            String prompt = createPrompt(expiringFoods);
//            CompletionDto completionDto = new CompletionDto("gpt-3.5-turbo-instruct", prompt, 0, 1000);
//            Map<String, Object> recipeResponse = chatGPTService.legacyPrompt(completionDto);
//
//            // API에서 반환된 choices 배열 내의 첫 번째 객체에서 text 값을 추출
//            List<Map<String, Object>> choices = (List<Map<String, Object>>) recipeResponse.get("choices");
//            String recipeMessage = "Recipe suggestions could not be retrieved.";
//            if (choices != null && !choices.isEmpty()) {
//                recipeMessage = (String) choices.get(0).get("text");
//            }
//            System.out.println(recipeMessage);
//
//            if (UserAccountController.sseEmitters.containsKey(username)) {
//                SseEmitter sseEmitterReceiver = UserAccountController.sseEmitters.get(username);
//                try {
//                    sseEmitterReceiver.send(SseEmitter.event().name("recipeSuggestion").data(recipeMessage));
//                } catch (Exception e) {
//                    UserAccountController.sseEmitters.remove(username);
//                }
//            }
//        }
//    }




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


}
