package com.hyunbenny.snsApplication.consumer;

import com.hyunbenny.snsApplication.model.event.AlarmEvent;
import com.hyunbenny.snsApplication.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmConsumer {

    private final AlarmService alarmService;
    @KafkaListener(topics = "${spring.kafka.topic.alarm}")
    public void consumeAlarm(AlarmEvent event, Acknowledgment ack) {
        log.info("AlarmConsumer consumeAlarm 호출 ");
        log.info("consume event : {}", event);
        alarmService.send(event.getAlarmType(), event.getAlarmArgs(), event.getReceiveUserId());
        ack.acknowledge();
    }

}
