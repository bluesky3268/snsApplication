package com.hyunbenny.snsApplication.model.event;

import com.hyunbenny.snsApplication.model.alarm.AlarmArgs;
import com.hyunbenny.snsApplication.model.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEvent {

    private Long receiveUserId;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
}
