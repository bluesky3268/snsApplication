package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.Comment;
import com.hyunbenny.snsApplication.model.Post;
import com.hyunbenny.snsApplication.model.alarm.AlarmArgs;
import com.hyunbenny.snsApplication.model.alarm.AlarmType;
import com.hyunbenny.snsApplication.model.entity.*;
import com.hyunbenny.snsApplication.model.event.AlarmEvent;
import com.hyunbenny.snsApplication.producer.AlarmProducer;
import com.hyunbenny.snsApplication.repository.*;
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
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final AlarmService alarmService;
    private final AlarmProducer alarmProducer;

    @Transactional
    public void create(String title, String content, String username) {
        // 유저를 찾아서 없으면 예외
        UserEntity userEntity = getUserOrElsException(username);

        // 게시글 저장
        postEntityRepository.save(PostEntity.of(title, content, userEntity));
    }

    @Transactional
    public Post modify(Long postId, String title, String content, String username) {
        // 유저가 있는 지 확인
        UserEntity userEntity =getUserOrElsException(username);
        
        // 게시글이 존재하는 지 확인
        PostEntity postEntity = getPostOrElsException(postId);

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
        UserEntity userEntity = getUserOrElsException(username);

        // 게시글이 존재하는 지 확인
        PostEntity postEntity = getPostOrElsException(postId);

//         게시글 작성자 == username 확인
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s does not have permission for %s", username, postId));
        }

        // 삭제
        likeEntityRepository.deleteAllByPost(postEntity);
        commentEntityRepository.deleteAllByPost(postEntity);
        postEntityRepository.deleteById(postId);
    }

    public Page<Post> getFeeds(Pageable pageable) {
        Page<PostEntity> feeds = postEntityRepository.findAll(pageable);
        return feeds.map(Post::fromPostEntity);
    }

    @Transactional
    public Page<Post> getMyPosts(String username, Pageable pageable) {
        // 유저가 있는 지 확인
        UserEntity userEntity = getUserOrElsException(username);

        // 유저가 있으면 해당 유저가 작성한 포스트 모두 가져오기
        Page<PostEntity> myPosts = postEntityRepository.findAllByUser(userEntity, pageable);

        return myPosts.map(Post::fromPostEntity);
    }

    @Transactional
    public long likesCount(Long postId) {
        // 게시글이 존재하는 지 확인
        PostEntity postEntity = getPostOrElsException(postId);

        return likeEntityRepository.countByPost(postEntity).longValue();
    }

    @Transactional
    public void likes(Long postId, String username) {
        // 유저가 있는 지 확인
        UserEntity userEntity = getUserOrElsException(username);

        // 게시글이 존재하는 지 확인
        PostEntity postEntity = getPostOrElsException(postId);

        // 해당 게시글에 이미 좋아요를 눌럿는지 확인 -> 이미 좋아요를 누른 경우 취소(좋아요 삭제)하기
        Optional<LikeEntity> likeEntity = likeEntityRepository.findByPostAndUser(postEntity, userEntity);
        likeEntity.ifPresent(like -> {
            log.info("already hit the like -> cancel like");
            likeEntityRepository.deleteById(like.getId());
        });

        // 기존에 누르지 않았으면 저장 후 알람 등록
        if (likeEntity.isEmpty()) {
            LikeEntity savedLikeEntity = likeEntityRepository.save(LikeEntity.of(postEntity, userEntity));
//            alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));

            // 알람 이벤트 발생
            alarmProducer.send(new AlarmEvent(postEntity.getUser().getId(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
        }

    }

    public Page<Comment> getComments(Long postId, Pageable pageable) {
        PostEntity postEntity = getPostOrElsException(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(entity -> Comment.fromCommentEntity(entity));
    }

    @Transactional
    public void createComment(Long postId, String comment, String username) {
        // 유저가 있는 지 확인
        UserEntity userEntity = getUserOrElsException(username);

        // 게시글이 존재하는 지 확인
        PostEntity postEntity = getPostOrElsException(postId);

        // 댓글 저장
        commentEntityRepository.save(CommentEntity.of(postEntity, comment, userEntity));
        log.info("댓글 저장 완료");

        // 알람 등록
//        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
//        log.info("알람 등록 완료");

        // 알람 발생 브라우저에 알리기
        log.info("카프카 프로듀서(alarmProducer) send() 호출");
        alarmProducer.send(new AlarmEvent(postEntity.getUser().getId(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));

    }

    private PostEntity getPostOrElsException(Long postId) {
        // 게시글이 존재하는 지 확인
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
    }

    private UserEntity getUserOrElsException(String username) {
        // 유저가 있는 지 확인
        return userEntityRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", username)));
    }
}
