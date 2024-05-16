package com.myproject.cleanplate.service;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.dto.request.FoodRequest;
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

    // 테스트용
//    @Transactional(readOnly = true)
//    public List<FoodDto> searchExpiration() {
//        return foodRepository.findByExpirationWithinThreeDays().stream().map(FoodDto::from).collect(Collectors.toList());
//    }



    public void updateFood(Long foodId, FoodDto dto) throws Exception {
        Food food = foodRepository.findById(foodId);
        UserAccount userAccount = userAccountRepository.findByUsername(dto.userAccountDto().username());


        if(food.getUserAccount().equals(userAccount)){
            if(dto.quantity() != null) {food.setQuantity(dto.quantity());}
            if(dto.storage() != null) {food.setStorage(dto.storage());}
            if(dto.expiration() != null) {food.setExpiration(dto.expiration());}
        }
        else{
            throw new Exception("잘못된 foodId 입니다.");
        }
        foodRepository.save(food);

    }


    public void deleteFood(Long foodId) throws Exception{
        foodRepository.deleteById(foodId);
    }

    public void deleteAll() throws Exception{
        foodRepository.deleteAll();
    }
}
