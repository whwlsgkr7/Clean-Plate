package com.myproject.cleanplate.controller;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/food")
@RestController
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/save")
    public ResponseEntity<?> saveFood(@RequestBody FoodDto dto){
        try {
            foodService.saveFood(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/searchFoodList")
    public List<FoodDto> searchFoodList(String userId){
        List<FoodDto> list = null;
        try{
            list = foodService.searchSavedFoods(userId);

        } catch (Exception e){
            e.printStackTrace();
        }
        return list;

    }

//    @GetMapping("/test")
//    public List<FoodDto> test(String userId, String foodName){
//        List<FoodDto> list = null;
//        try{
//            list = foodService.test(userId, foodName);
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return list;
//
//    }

    @PatchMapping("/updateFood")
    public ResponseEntity<?> updateFood(@RequestBody FoodDto foodDto){
        try {
            foodService.updateFood(foodDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
        return ResponseEntity.ok().body("success");

    }

    @DeleteMapping("/deleteFood/{userId}/{foodName}")
    public ResponseEntity<?> deleteFood(@PathVariable String userId, @PathVariable String foodName){
        try {
            foodService.deleteFood(userId, foodName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }
        return ResponseEntity.ok().body("success");
    }


}
