package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.Post;
import com.hyunbenny.snsApplication.model.entity.PostEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import com.hyunbenny.snsApplication.repository.PostEntityRepository;
import com.hyunbenny.snsApplication.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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

    @Transactional
    public Post modify(Long postId, String title, String content, String username) {
        // 유저가 있는 지 확인
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", username)));
        
        // 게시글이 존재하는 지 확인
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));

        // 게시글 작성자 == username 확인
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("$s does not have permission for %s", username, postId));
        }

        // 포스트 수정
        postEntity.updatePost(title, content);

        return Post.fromPostEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(Long postId, String username) {
        // 유저가 있는 지 확인
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", username)));

        // 게시글이 존재하는 지 확인
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));

//         게시글 작성자 == username 확인
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s does not have permission for %s", username, postId));
        }

        // 삭제
        postEntityRepository.deleteById(postId);
    }

    public Page<Post> getFeeds(Pageable pageable) {
        Page<PostEntity> feeds = postEntityRepository.findAll(pageable);
        return feeds.map(Post::fromPostEntity);
    }

    public Page<Post> getMyPosts(String username, Pageable pageable) {
        // 유저가 있는 지 확인
        UserEntity userEntity = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", username)));

        // 유저가 있으면 해당 유저가 작성한 포스트 모두 가져오기
        Page<PostEntity> myPosts = postEntityRepository.findAllByUser(userEntity, pageable);

        return myPosts.map(Post::fromPostEntity);
    }
}
