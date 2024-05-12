package com.myproject.cleanplate.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    EXPIRATION_WITHIN_THREEDAYS("near expiration!"),
    RECIPE_RECOMMENDATION("new recipe!");

    private final String alarmText;
}
