package com.myproject.cleanplate.repository;


import com.myproject.cleanplate.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    List<Restaurant> findByRestaurantName(String restaurantName);
    List<Restaurant> findBySanitaryGrade(String sanitaryGrade);
}
