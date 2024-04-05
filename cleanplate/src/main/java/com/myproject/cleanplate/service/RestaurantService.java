package com.myproject.cleanplate.service;

import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.RestaurantDto;
import com.myproject.cleanplate.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public void saveRestaurant(RestaurantDto dto) {
        try {
            restaurantRepository.save(dto.toEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<RestaurantDto> searchBySanitary(String sanitary) throws Exception {
        List<RestaurantDto> list = null;
        if (sanitary != null) {
            list = restaurantRepository.findBySanitaryGrade(sanitary).stream().map(RestaurantDto::from).collect(Collectors.toList());

        }
        return list;
    }

    public List<RestaurantDto> searchByName(String name) throws Exception{
        return restaurantRepository.findByRestaurantName(name).stream().map(RestaurantDto::from).collect(Collectors.toList());
    }

    public void deleteAll() throws Exception{
        restaurantRepository.deleteAll();
    }



}
