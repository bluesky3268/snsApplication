package com.hyunbenny.snsApplication.controller.response;

import com.hyunbenny.snsApplication.model.Alarm;
import com.hyunbenny.snsApplication.model.User;
import com.hyunbenny.snsApplication.model.alarm.AlarmArgs;
import com.hyunbenny.snsApplication.model.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getAlarmArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getCreatedAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
