package com.myproject.cleanplate.service;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.dto.response.FoodResponse;
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
//    private final AlarmService alarmService;


    public void saveFood(FoodDto dto){
        try {
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().username());
            foodRepository.save(dto.toEntity(userAccount));
        } catch (Exception e) {
            log.warn("음식 저장 실패. 음식 저장에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<FoodResponse> searchSavedFoods(String username){

        return foodRepository.findByUserAccount_UsernameOrderByExpirationAsc(username).stream().map(FoodResponse::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoodDto> searchExpiration() {
        return foodRepository.findByExpirationWithinThreeDays().stream().map(FoodDto::from).collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    @Scheduled(fixedRate = 1000)
//    public void checkAndNotifyExpiringFoods() {
//        List<FoodDto> expiringFoods = foodRepository.findByExpirationWithinThreeDays()
//                .stream()
//                .map(FoodDto::from)
//                .collect(Collectors.toList());
//        for (FoodDto food : expiringFoods) {
//            try {
//                alarmService.send(1, food.userAccountDto().username(), food.foodName());
//            } catch (Exception e) {
//                log.error("Failed to send alarm for food: {}", food.foodName(), e);
//            }
//        }
//    }

//    @Transactional(readOnly = true)
//    public List<FoodDto> test(String userId, String foodName){
//
//        return foodRepository.findByUserAccount_UserIdAndFoodName(userId, foodName).stream().map(FoodDto::from).collect(Collectors.toList());
//    }

    // TODO: username 또는 foodName이 없을 경우 예외 던지기 처리하기
    public void updateFood(FoodDto dto) throws Exception {
        // 사용자 ID로 해당 사용자의 특정 food를 조회
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


    public void deleteFood(String username, String foodName) throws Exception{
        foodRepository.deleteByUserAccount_UsernameAndFoodName(username, foodName);
    }

    public void deleteAll() throws Exception{
        foodRepository.deleteAll();
    }
}
