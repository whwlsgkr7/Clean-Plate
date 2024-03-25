package com.myproject.cleanplate.service;

import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.RestaurantDto;
import com.myproject.cleanplate.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public void saveRestaurant(RestaurantDto dto) {
        try {
            restaurantRepository.save(dto.toEntity(dto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
