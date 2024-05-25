package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.dto.ChatCompletionDto;
import com.myproject.cleanplate.dto.ChatRequestMsgDto;
import com.myproject.cleanplate.dto.CompletionDto;
import com.myproject.cleanplate.dto.request.RecipeRequest;
import com.myproject.cleanplate.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chatGpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;


    @PostMapping("/recommendRecipe")
    public ResponseEntity<Map<String, Object>> recommendRecipe(@RequestBody RecipeRequest recipeRequest) {
        List<ChatRequestMsgDto> requestMsgDtoList = chatGPTService.createPromptUser(recipeRequest);

        // ChatCompletionDto 객체 생성
        ChatCompletionDto chatCompletionDto = ChatCompletionDto.builder()
                .model("gpt-3.5-turbo-0125")
                .messages(requestMsgDtoList)
                .build();

        // chatGPTService 호출
        Map<String, Object> recipeResponse = chatGPTService.prompt(chatCompletionDto);

        // api 응답에서 레시피 추천 content를 뽑는 과정
        String recipeMessage = "레시피 검색을 실패하였습니다.";
        if (recipeResponse.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) recipeResponse.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null && message.containsKey("content")) {
                    recipeMessage = (String) message.get("content");

                    // 클라이언트에게 반환할 Map 생성
                    Map<String, Object> response = new HashMap<>();
                    response.put("recipe", recipeMessage);

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }

        }
        // 실패 응답
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Failed to retrieve recipe.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    // ChatGPT 모델 리스트를 조회
    @GetMapping("/modelList")
    public ResponseEntity<List<Map<String, Object>>> selectModelList() {
        List<Map<String, Object>> result = chatGPTService.modelList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // 해당 ChatGPT 모델이 유효한지 조회
    @GetMapping("/model")
    public ResponseEntity<Map<String, Object>> isValidModel(@RequestParam(name = "modelName") String modelName) {
        Map<String, Object> result = chatGPTService.isValidModel(modelName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}