package com.hyunbenny.snsApplication.model;

import com.hyunbenny.snsApplication.model.alarm.AlarmArgs;
import com.hyunbenny.snsApplication.model.alarm.AlarmType;
import com.hyunbenny.snsApplication.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Long id;
    private User user;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Alarm fromEntity(AlarmEntity entity) {
        return new Alarm(
                entity.getId(),
                User.fromEntity(entity.getUser()),
                entity.getAlarmType(),
                entity.getAlarmArgs(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
