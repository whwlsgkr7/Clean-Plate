package com.myproject.cleanplate.repository;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.QFood;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface FoodRepository extends JpaRepository<Food, String>{

    List<Food> findByUserAccount_UserId(String userId);
    List<Food> findByUserAccount_UserIdAndFoodName(String userId, String foodName);

    void deleteByUserAccount_UserIdAndFoodName(String userId, String foodName);


}
