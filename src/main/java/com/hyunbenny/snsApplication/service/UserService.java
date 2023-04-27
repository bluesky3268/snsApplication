package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.Alarm;
import com.hyunbenny.snsApplication.model.User;
import com.hyunbenny.snsApplication.model.entity.AlarmEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import com.hyunbenny.snsApplication.repository.AlarmEntityRepository;
import com.hyunbenny.snsApplication.repository.UserCacheRepository;
import com.hyunbenny.snsApplication.repository.UserEntityRepository;
import com.hyunbenny.snsApplication.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserCacheRepository userCacheRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.token.secretKey}")
    private String secretKey;

    @Value("${jwt.token.expiredMs}")
    private Long expiredMs;

    public User loadByUsername(String username) {
        return userCacheRepository.getUser(username).orElseGet(() ->
                userEntityRepository.findByUsername(username).map(User::fromEntity).orElseThrow(()
                -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", username))));
    }

    @Transactional
    public User join(String username, String password) {
        // 가입하려는 username으로 등록된 User가 있는지 확인
        userEntityRepository.findByUsername(username).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("username : %s", username));
        });

        // 없으면 회원가입 진행
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(username, bCryptPasswordEncoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String username, String password) {
        // 가입한 유저인지 확인
        User user = loadByUsername(username);

        // 유저 레디스 캐시에 세팅
        userCacheRepository.setUser(user);

        // 비밀번호 일치여부 확인
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        return JwtTokenUtils.generateToken(username, secretKey, expiredMs);
    }

    public Page<Alarm> alarmList(Long userId, Pageable pageable) {
        return alarmEntityRepository.findAllByUserId(userId, pageable).map(entity -> Alarm.fromEntity(entity));
    }
}
