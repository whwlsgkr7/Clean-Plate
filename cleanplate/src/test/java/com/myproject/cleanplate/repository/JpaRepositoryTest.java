package com.myproject.cleanplate.repository;

import com.myproject.cleanplate.config.JpaConfig;
import com.myproject.cleanplate.domain.Food;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트") // 해당 테스트 이름 정의
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaRepositoryTest {
    private final FoodRepository foodRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(@Autowired FoodRepository foodRepository, @Autowired UserAccountRepository userAccountRepository) {
        this.foodRepository = foodRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Disabled
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Food> food = foodRepository.findAll();

        // Then
        assertThat(food)
                .isNotNull()
                .hasSize(6);
    }
}
