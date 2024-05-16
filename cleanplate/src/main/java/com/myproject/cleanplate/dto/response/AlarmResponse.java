package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.Alarm;
import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.domain.constant.AlarmType;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record AlarmResponse(Long id,
                       String alarmType,
                       String content,
                       LocalDateTime createdAt,
                            LocalDateTime modifiedAt,
                            LocalDateTime removedAt
) {

    public static AlarmResponse fromDto(AlarmDto dto){
        return new AlarmResponse(
                dto.id(),
                dto.alarmType(),
                dto.content(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.removedAt()
        );
    }

    public static AlarmResponse fromEntity(Alarm Entity){
        return new AlarmResponse(
                Entity.getId(),
                Entity.getAlarmType().getAlarmText(),
                Entity.getContent(),
                Entity.getCreatedAt(),
                Entity.getModifiedAt(),
                Entity.getRemovedAt()
        );
    }
}
