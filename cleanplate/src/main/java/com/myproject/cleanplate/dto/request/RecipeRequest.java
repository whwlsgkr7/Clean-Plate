package com.myproject.cleanplate.dto.request;

import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;

import java.util.List;

public record RecipeRequest(List<String> ingredients,
                            String cuisineType,
                            List<String> dietaryRestrictions) {




}
