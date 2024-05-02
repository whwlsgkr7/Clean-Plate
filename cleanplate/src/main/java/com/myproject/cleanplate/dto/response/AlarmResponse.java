package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Alarm;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.domain.constant.AlarmArgs;
import com.myproject.cleanplate.domain.constant.AlarmType;
import lombok.Getter;

import java.sql.Timestamp;

public record AlarmResponse(Integer id,
                       AlarmType alarmType,
//                       AlarmArgs alarmArgs,
                       String text,
                       Timestamp createdAt,
                       Timestamp modifiedAt,
                       Timestamp removedAt
) {

    public static AlarmResponse fromEntity(Alarm alarm){
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
//                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getCreatedAt(),
                alarm.getModifiedAt(),
                alarm.getRemovedAt()
        );
    }
}
