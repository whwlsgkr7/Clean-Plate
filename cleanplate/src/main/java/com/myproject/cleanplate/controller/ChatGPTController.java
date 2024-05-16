package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.dto.ChatCompletionDto;
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
@RequestMapping(value = "/api/v1/chatGpt")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

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


    // Legacy ChatGPT 프롬프트 명령을 수행 : gpt-3.5-turbo-instruct, babbage-002, davinci-002
    @PostMapping("/legacyPrompt")
    public ResponseEntity<Map<String, Object>> selectLegacyPrompt(@RequestBody CompletionDto completionDto) {
        log.debug("param :: " + completionDto.toString());
        Map<String, Object> result = chatGPTService.legacyPrompt(completionDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/recommendRecipe")
    public ResponseEntity<Map<String, Object>> recommendRecipe(@RequestBody RecipeRequest recipeRequest) {
        String prompt = chatGPTService.createPromptUser(recipeRequest);

        // CompletionDto 객체 생성
        CompletionDto completionDto = CompletionDto.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt(prompt)
                .temperature(0)
                .max_tokens(1000)
                .build();

        // chatGPTService 호출
        Map<String, Object> result = chatGPTService.legacyPrompt(completionDto);

        // choices 배열에서 text 추출
        if (result.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
            if (!choices.isEmpty() && choices.get(0).containsKey("text")) {
                String text = (String) choices.get(0).get("text");

                // 클라이언트에게 반환할 Map 생성
                Map<String, Object> response = new HashMap<>();
                response.put("alarm", text);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        // 실패 응답
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Failed to retrieve recipe.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    // 최신 ChatGPT 프롬프트 명령어를 수행 : gpt-4, gpt-4 turbo, gpt-3.5-turbo
    @PostMapping("/prompt")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDto chatCompletionDto) {
        log.debug("param :: " + chatCompletionDto.toString());
        Map<String, Object> result = chatGPTService.prompt(chatCompletionDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}