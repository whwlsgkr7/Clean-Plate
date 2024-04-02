package com.myproject.cleanplate.repository.querydsl;

import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.QFood;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.selectFrom;

public class FoodRepositoryCustomImpl implements FoodRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Food> findByExpirationWithinThreeDays() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QFood food = QFood.food;

        // 현재 날짜
        LocalDate today = LocalDate.now();

        // 현재 날짜로부터 3일 후의 날짜
        LocalDate threeDaysLater = LocalDate.now().plusDays(3);

        // 오늘과 3일 후 사이에 유통기한이 있는 음식을 찾는다.
        return queryFactory.selectFrom(food)
                .where(food.expiration.between(
                        Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(threeDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant())
                ))
                .fetch();
    }
}
