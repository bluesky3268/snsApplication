package com.hyunbenny.snsApplication.model;

import com.hyunbenny.snsApplication.model.alarm.AlarmArgs;
import com.hyunbenny.snsApplication.model.alarm.AlarmType;
import com.hyunbenny.snsApplication.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;

@Slf4j
@Getter
@AllArgsConstructor
public class Alarm {
    private Long id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        log.info("==== called Alarm fromEntity() =====");
        return new Alarm(
                entity.getId(),
                entity.getAlarmType(),
                entity.getAlarmArgs(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
