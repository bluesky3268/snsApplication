package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.AlarmNoti;
import com.hyunbenny.snsApplication.model.alarm.AlarmArgs;
import com.hyunbenny.snsApplication.model.alarm.AlarmType;
import com.hyunbenny.snsApplication.model.entity.AlarmEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import com.hyunbenny.snsApplication.repository.AlarmEntityRepository;
import com.hyunbenny.snsApplication.repository.EmitterRepository;
import com.hyunbenny.snsApplication.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";
    private final EmitterRepository emitterRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserEntityRepository userEntityRepository;


    public void send(AlarmType alarmType, AlarmArgs alarmArgs, Long receiveUserId) {
        log.info("AlarmService send() : 클라이언트에 알람 보내기");
        UserEntity userEntity = userEntityRepository.findById(receiveUserId).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        // alarm save
        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(userEntity, alarmType, alarmArgs));


        // sse가 브라우저당 하나당 생기는데 서버단에서는 누구한테 알람이 왔는지 찾아서 보내줘야 한다.
        // 그러기 위해서는 서버측에 저장을 해둬야 하는데 이를 위한 클래스가 -> EmitterRepository(로컬리포지토리)
        emitterRepository.get(receiveUserId).ifPresentOrElse(sseEmitter -> {
            try{
                sseEmitter.send(SseEmitter.event()
                        .id(String.valueOf(alarmEntity.getId()))
                        .name(ALARM_NAME)
                        .data("new alarm"));
            } catch (IOException e) {
                emitterRepository.delete(receiveUserId);
                throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("no emmitter found"));

    }

    public SseEmitter connectAlarm(Long userId) {
        log.info("connectAlarm()");
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIME_OUT);
        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() ->  emitterRepository.delete(userId));
        sseEmitter.onTimeout(() ->  emitterRepository.delete(userId));

        try{
            // send를 통해서 이벤트 전송
            // id : 발급된 이벤트의 아이디(서버에서는 마지막으로 전송한 데이터를 갖고 있는다 : 데이터 전송 중 유실을 대비 + 해당 아이디 이후의 데이터를 모두 전송하는 등으로 사용할 수 있다.)
            log.info("alarm send");
            sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect complete"));
        } catch (IOException e) {
            throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
        }

        return sseEmitter;
    }
}
