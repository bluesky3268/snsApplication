package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.entity.PostEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import com.hyunbenny.snsApplication.repository.PostEntityRepository;
import com.hyunbenny.snsApplication.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String content, String username) {
        // 유저를 찾아서 없으면 예외
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", username)));

        // 게시글 저장
        postEntityRepository.save(PostEntity.of(title, content, userEntity));
    }
}
