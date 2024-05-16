package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.dto.CustomUserDetails;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.dto.request.FoodRequest;
import com.myproject.cleanplate.dto.response.FoodResponse;
import com.myproject.cleanplate.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/food")
@RestController
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/save")
    public ResponseEntity<?> saveFood(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                      @RequestBody FoodRequest foodRequest){
        try {
            foodService.saveFood(foodRequest.toDtoSave(customUserDetails.toDto()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/searchFoodList")
    public List<FoodResponse> searchFoodList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<FoodResponse> list = null;
        String username = customUserDetails.toDto().username();
        try{
            list = foodService.searchSavedFoods(username);

        } catch (Exception e){
            e.printStackTrace();
        }
        return list;

    }

    // 테스트용
//    @GetMapping("/searchExpiration")
//    public List<FoodDto> searchExpiration(){
//        List<FoodDto> list = null;
//        try{
//            list = foodService.searchExpiration();
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//        return list;
//
//    }


    @PatchMapping("/updateFood/{foodId}")
    public ResponseEntity<?> updateFood(@PathVariable Long foodId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @RequestBody FoodRequest foodRequest){
        try {
            foodService.updateFood(foodId, foodRequest.toDtoUpdate(customUserDetails.toDto()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
        return ResponseEntity.ok().body("success");

    }

    @DeleteMapping("/deleteFood/{foodId}")
    public ResponseEntity<?> deleteFood(@PathVariable Long foodId){
        try {
            foodService.deleteFood(foodId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }
        return ResponseEntity.ok().body("success");
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAll(){
        try {
            foodService.deleteAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok("success");
    }



}
