package com.myproject.cleanplate.service;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.repository.FoodRepository;
import com.myproject.cleanplate.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FoodService {
    private final FoodRepository foodRepository;
    private final UserAccountRepository userAccountRepository;


    public void saveFood(FoodDto dto){
        try {
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().username());
            foodRepository.save(dto.toEntity(userAccount));
        } catch (Exception e) {
            log.warn("음식 저장 실패. 음식 저장에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<FoodDto> searchSavedFoods(String userId){

        return foodRepository.findByUserAccount_Username(userId).stream().map(FoodDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true) @Scheduled(fixedRate = 1000)
    public List<FoodDto> searchExpiration() {
        return foodRepository.findByExpirationWithinThreeDays().stream().map(FoodDto::from).collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<FoodDto> test(String userId, String foodName){
//
//        return foodRepository.findByUserAccount_UserIdAndFoodName(userId, foodName).stream().map(FoodDto::from).collect(Collectors.toList());
//    }

    public void updateFood(FoodDto dto) throws Exception {
        // 사용자 ID로 해당 사용자의 모든 Food 엔티티를 조회
        List<Food> foods = foodRepository.findByUserAccount_UsernameAndFoodName(dto.userAccountDto().username(), dto.foodName());

        for (Food food : foods) {
            if (dto.expiration() != null) {
                food.setExpiration(dto.expiration());
            }
            if (dto.storage() != null) {
                food.setStorage(dto.storage());
            }
            if (dto.quantity() != null) {
                food.setQuantity(dto.quantity());
            }

            // 업데이트된 엔티티 저장
            foodRepository.save(food);

        }
    }


    public void deleteFood(String userId, String foodName) throws Exception{
        foodRepository.deleteByUserAccount_UsernameAndFoodName(userId, foodName);

    }
}
