package com.myproject.cleanplate.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.cleanplate.config.ChatGPTConfig;
import com.myproject.cleanplate.dto.ChatCompletionDto;
import com.myproject.cleanplate.dto.ChatRequestMsgDto;
import com.myproject.cleanplate.dto.CompletionDto;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.request.RecipeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;


    @Value("${openai.url.model}")
    private String modelUrl;

    @Value("${openai.url.model-list}")
    private String modelListUrl;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    @Value("${openai.url.legacy-prompt}")
    private String legacyPromptUrl;

    // 사용 가능한 모델 리스트를 조회
    public List<Map<String, Object>> modelList() {
        log.debug("[+] 모델 리스트를 조회합니다.");
        List<Map<String, Object>> resultList = null;
        // [STEP1] 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();
        System.out.println(modelUrl);
        // [STEP2] 통신을 위한 RestTemplate을 구성한다.
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(modelUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            ObjectMapper om = new ObjectMapper();
            Map<String, Object> data = om.readValue(response.getBody(), new TypeReference<>() {});

            // [STEP4] 응답 값을 결과값에 넣고 출력을 해본다.
            resultList = (List<Map<String, Object>>) data.get("data");
            for (Map<String, Object> object : resultList) {
                log.debug("ID: " + object.get("id"));
                log.debug("Object: " + object.get("object"));
                log.debug("Created: " + object.get("created"));
                log.debug("Owned By: " + object.get("owned_by"));
            }
        } catch (Exception e) {
            log.debug("Exception :: " + e.getMessage());
        }
        return resultList;
    }

    // 모델이 유효한지 확인하는 로직
    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] 모델이 유효한지 조회합니다. 모델 : " + modelName);
        Map<String, Object> result = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP2] 통신을 위한 RestTemplate을 구성한다.
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(modelListUrl + "/" + modelName, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            // [STEP3] Jackson을 기반으로 응답값을 가져온다.
            ObjectMapper om = new ObjectMapper();
            result = om.readValue(response.getBody(), new TypeReference<>() {});
        } catch (Exception e) {
            log.debug("Exception :: " + e.getMessage());
        }
        return result;
    }


    // chatgpt 프롬프트 검색
    public Map<String, Object> legacyPrompt(CompletionDto completionDto) {
        log.debug("[+] 레거시 프롬프트를 수행합니다.");

        // [STEP1] 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP2] 통신을 위한 RestTemplate을 구성한다.
        HttpEntity<CompletionDto> requestEntity = new HttpEntity<>(completionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(legacyPromptUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            ObjectMapper om = new ObjectMapper();
            // [STEP3] String -> HashMap 역직렬화를 구성한다.
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {});
        } catch (Exception e) {
            log.debug("Exception :: " + e.getMessage());
        }
        return resultMap;
    }


    public String createLegacyPromptUser(RecipeRequest recipeRequest) {
        String ingredientList = String.join(", ", recipeRequest.ingredients());

        // 요리 유형과 식단 제한을 포함한 추가적인 설명
        String cuisineType = recipeRequest.cuisineType();
        String dietaryRestrictions = String.join(", ", recipeRequest.dietaryRestrictions());

        String prompt = "당신은 이제부터 세계 최고의 요리사입니다. 다음 재료: " + ingredientList +
                "를 사용하여 " + (cuisineType.isEmpty() ? "" : cuisineType + " 스타일의 ") +
                "레시피를 제안해주세요." +
                (!dietaryRestrictions.isEmpty() ? " 다음 식단 제한을 고려하세요: " + dietaryRestrictions + "." : "") +
                " 요리 이름, 재료, 요리 설명 순서대로 언급해주세요.";

        return prompt;
    }



    // 신규 모델에 대한 프롬프트
    public Map<String, Object> prompt(ChatCompletionDto chatCompletionDto) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");
        Map<String, Object> resultMap = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져온다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP2] 통신을 위한 RestTemplate을 구성한다. HttpEntity(){body, header}
        HttpEntity<ChatCompletionDto> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // [STEP3] String -> HashMap 역직렬화를 구성한다.
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {});
        } catch (Exception e) {
            log.debug("Exception :: " + e.getMessage());
        }
        return resultMap;
    }


    public List<ChatRequestMsgDto> createPromptUser(RecipeRequest recipeRequest) {
        String ingredientList = String.join(", ", recipeRequest.ingredients());

        // 요리 유형과 식단 제한을 포함한 추가적인 설명
        String cuisineType = recipeRequest.cuisineType();
        String dietaryRestrictions = String.join(", ", recipeRequest.dietaryRestrictions());


        ChatRequestMsgDto chatRequestMsgDto = ChatRequestMsgDto.builder()
                .role("system")
                .content("당신은 이제부터 세계 최고의 요리사입니다. 다음 재료: " + ingredientList +
                        "를 사용하여 " + (cuisineType.isEmpty() ? "" : cuisineType + " 스타일의 ") +
                        "레시피를 제안해주세요." +
                        (!dietaryRestrictions.isEmpty() ? " 다음 식단 제한을 고려하세요: " + dietaryRestrictions + "." : "") +
                        " 요리 이름, 재료, 요리 설명(재료 준비 방법, 요리 과정) 각 구간으로 나눠서 순서대로 언급해주세요.  조리방 법에는 " +
                        "각 과정에서 소제목과 조리 시간을 언급해주세요. 예를들면 해당 재료를 얼마나 익혀야하는지 불의 세기 등등.")
                .build();

        List<ChatRequestMsgDto> chatRequestMsgDtoList = new ArrayList<>();
        chatRequestMsgDtoList.add(chatRequestMsgDto);

        return chatRequestMsgDtoList;


    }


}

