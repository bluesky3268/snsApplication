package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.User;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import com.hyunbenny.snsApplication.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    //TODO : 회원가입 기능 구현
    public User join(String username, String password) {
        // 가입하려는 username으로 등록된 User가 있는지 확인
        Optional<UserEntity> userEntity = userEntityRepository.findByUsername(username);

        // 없으면 회원가입 진행
        userEntityRepository.save(new UserEntity());

        return new User();
    }

    //TODO : 로그인 기능 구현
    public String login(String username, String password) {
        // 가입한 유저인지 확인
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() -> new SnsApplicationException());


        // 비밀번호 일치여부 확인
        if(!userEntity.getPassword().equals(password)) throw new SnsApplicationException();
        
        // 토큰 생성

        return "";
    }
}
