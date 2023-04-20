package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.fixture.PostFixture;
import com.hyunbenny.snsApplication.fixture.UserFixture;
import com.hyunbenny.snsApplication.model.entity.PostEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;
import com.hyunbenny.snsApplication.repository.PostEntityRepository;
import com.hyunbenny.snsApplication.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 포스트작성_성공(){
        String title = "test title";
        String content = "test content";
        String username = "username";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        Assertions.assertDoesNotThrow(() -> postService.create(title, content, username));
    }

    @Test
    void 포스트작성_요청한_유저가_존재하지_않는_경우(){
        String title = "test title";
        String content = "test content";
        String username = "username";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, content, username));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트수정_성공(){
        // userInfo
        Long userId = 1L;
        String username = "username";
        String password = "password";

        // oldPostInfo
        Long postId = 1L;
        String title = "test title";
        String content = "test content";

        // newPostInfo
        String modiTitle = "modi title";
        String modiContent = "modi content";

        PostEntity postFixture = PostFixture.getPostEntity(postId, title, content, userId, username, password);
        UserEntity userFixture = postFixture.getUser();

        PostEntity modiPostFixture = PostFixture.getPostEntity(postId, modiTitle, modiContent, userId, username, password);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userFixture));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postFixture));
        when(postEntityRepository.saveAndFlush(modiPostFixture)).thenReturn(modiPostFixture);

        Assertions.assertDoesNotThrow(() -> postService.modify(postId, modiTitle, modiContent, username));
    }

    @Test
    void 포스트수정_해당_포스트가_존재하지_않는_경우(){
        Long postId = 1L;
        String title = "test title";
        String content = "test content";
        Long userId = 1L;
        String username = "username";
        String password = "password";

        PostEntity postFixture = PostFixture.getPostEntity(postId, title, content, userId, username, password);
        UserEntity userFixture = postFixture.getUser();

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(userFixture));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, content, username));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트수정_권한이_없는_경우(){
        Long postId = 1L;
        String title = "test title";
        String content = "test content";
        Long userId = 1L;
        String username = "username";
        String password = "password";

        PostEntity postFixture = PostFixture.getPostEntity(postId, title, content, userId, username, password);
        UserEntity userFixture = postFixture.getUser();

        UserEntity writer = UserFixture.getUserEntity(2L, "wrongUsername", "password");

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postFixture));

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, content, username));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void 포스트삭제_성공(){
        PostEntity post = PostFixture.getPostEntity(1L, "title", "content", 1L, "user", "password");
        UserEntity user = post.getUser();

        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(postEntityRepository.findById(post.getId())).thenReturn(Optional.of(post));

        Assertions.assertDoesNotThrow(() -> postService.delete(post.getId(), user.getUsername()));
    }

    @Test
    void 포스트삭제_해당_유저가_없는_경우(){
        Long postId = 1L;
        String username = "user";

        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(postId, username));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트삭제_해당_포스트가_존재하지_않는_경우(){
        Long postId = 1L;
        String username = "user";

        PostEntity post = PostFixture.getPostEntity(postId, "title", "content", 1L, username, "password");
        UserEntity user = post.getUser();

        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(postEntityRepository.findById(post.getId())).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(postId, username));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트삭제_권한이_없는_경우(){
        Long postId = 1L;
        String username = "user";

        PostEntity post = PostFixture.getPostEntity(postId, "title", "content", 1L, username, "password");
        UserEntity writer = UserFixture.getUserEntity(2L, "wrongUsername", "password");

        when(userEntityRepository.findByUsername(any())).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(post));

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(postId, username));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

}
