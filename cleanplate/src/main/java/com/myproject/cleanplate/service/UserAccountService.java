package com.myproject.cleanplate.service;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;


//    public List<FoodDto> searchFoodList()
//    List<FoodDto> foodList = userAccountRepository.findById()
}
