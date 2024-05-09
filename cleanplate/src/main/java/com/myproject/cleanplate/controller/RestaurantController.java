package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.dto.RestaurantDto;
import com.myproject.cleanplate.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/restaurant")
@RestController
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Value("${publicData.secretKey}")
    private String baseUrl;

    @GetMapping("/api")
    public ResponseEntity<?> loadJsonFromApi() {
        int pageSize = 1000; // 한 번에 요청할 데이터의 양
        int totalData = 100; // 전체 데이터의 양, 실제 API에서 제공하는 전체 데이터의 양을 기준으로 설정

        for (int i = 1; i <= totalData; i += pageSize) {
            String apiUrl = baseUrl + i + "/" + (i + pageSize - 1);
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("content-type", "application/json");

                BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                // BufferedReader로부터 모든 라인을 읽어서 이를 단일 문자열로 결합
                String result = bf.lines().collect(Collectors.joining());

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

                JSONObject c004Object = (JSONObject) jsonObject.get("C004");
                JSONArray jsonArray = (JSONArray) c004Object.get("row");

                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject object = (JSONObject) jsonArray.get(j);

                    String address = (String) object.get("ADDR");
                    String restaurantName = (String) object.get("BSSH_NM");
                    String phoneNumber = (String) object.get("TELNO");
                    String sanitaryGrade = (String) object.get("HG_ASGN_LV");
                    String assignYMD = (String) object.get("HG_ASGN_YMD");
                    String presidentName = (String) object.get("PRSDNT_NM");

                    RestaurantDto restaurantDto = RestaurantDto.of(address, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
                    restaurantService.saveRestaurant(restaurantDto);
                }

            } catch (Exception e) {
                e.printStackTrace();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
            }
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/sanitary")
    public List<RestaurantDto> searchBySanitary(String sanitary){
        List<RestaurantDto> list = null;
        try {
            list = restaurantService.searchBySanitary(sanitary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @GetMapping("/name")
    public List<RestaurantDto> searchByName(String name){
        List<RestaurantDto> list = null;
        try {
            list = restaurantService.searchByName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAll(){
        try{
            restaurantService.deleteAll();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
        return ResponseEntity.ok("success");
    }


}
