package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Alarm;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.domain.constant.AlarmType;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record AlarmDto(Long id,
                       UserAccountDto userAccountDto,
                       String alarmType,
                       String content,
                       LocalDateTime createdAt,
                       LocalDateTime modifiedAt,
                       LocalDateTime removedAt
                       ) {

    public static AlarmDto fromEntity(Alarm entity){
        return new AlarmDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getAlarmType().getAlarmText(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getRemovedAt()
        );
    }
}
