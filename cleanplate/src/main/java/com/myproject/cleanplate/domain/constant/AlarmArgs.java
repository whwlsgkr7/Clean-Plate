package com.myproject.cleanplate.domain.constant;

import lombok.Data;

@Data
public class AlarmArgs {
    // 알람을 발생시킨 사람
    private String fromUsername;
}
