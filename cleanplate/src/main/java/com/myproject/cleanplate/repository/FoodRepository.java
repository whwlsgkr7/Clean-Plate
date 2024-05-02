package com.myproject.cleanplate.repository;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.QFood;
import com.myproject.cleanplate.repository.querydsl.FoodRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FoodRepository extends JpaRepository<Food, String>, FoodRepositoryCustom {

    List<Food> findByUserAccount_UsernameOrderByExpirationAsc(String username);
    // food 상태 업데이트
    List<Food> findByUserAccount_UsernameAndFoodName(String userId, String foodName);
    List<Food> findByExpirationWithinThreeDays();

    void deleteByUserAccount_UsernameAndFoodName(String username, String foodName);


}
