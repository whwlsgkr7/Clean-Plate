package com.myproject.cleanplate.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    EXPIRATION_WITHIN_THREEDAYS("near expiration!");

    private final String alarmText;
}
