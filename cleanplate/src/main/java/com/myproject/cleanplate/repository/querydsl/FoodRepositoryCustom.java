package com.myproject.cleanplate.repository.querydsl;

import com.myproject.cleanplate.domain.Food;

import java.util.List;

public interface FoodRepositoryCustom {
    List<Food> findByExpirationWithinThreeDays();
}
