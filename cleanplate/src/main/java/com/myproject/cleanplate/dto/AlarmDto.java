package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Alarm;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.domain.constant.AlarmArgs;
import com.myproject.cleanplate.domain.constant.AlarmType;
import lombok.Getter;

import java.sql.Timestamp;

public record AlarmDto(Integer id,
                       UserAccountDto userAccountDto,
                       AlarmType alarmType,
//                       AlarmArgs alarmArgs,
                       Timestamp createdAt,
                       Timestamp modifiedAt,
                       Timestamp removedAt
                       ) {

    public static AlarmDto fromEntity(Alarm entity){
        return new AlarmDto(
                entity.getId(),
                UserAccountDto.from(entity.getUsername()),
                entity.getAlarmType(),
//                entity.getArgs(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getRemovedAt()
        );
    }
}
