package com.hyunbenny.snsApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunbenny.snsApplication.controller.request.PostCreateRequest;
import com.hyunbenny.snsApplication.controller.request.PostModifyRequest;
import com.hyunbenny.snsApplication.controller.response.PostModifyResponse;
import com.hyunbenny.snsApplication.controller.response.Response;
import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.fixture.PostFixture;
import com.hyunbenny.snsApplication.model.Post;
import com.hyunbenny.snsApplication.model.entity.PostEntity;
import com.hyunbenny.snsApplication.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser
    void 포스트작성_성공() throws Exception {
        String title = "test";
        String content = "test content";
        String username = "username";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, content))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트작성_로그인하지않은_경우_실패() throws Exception {
        String title = "test";
        String content = "test content";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, content))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트수정_성공() throws Exception {
        // given
        Long postId = 1L;
        String title = "title";
        String content = "content";

        PostEntity postEntity = PostFixture.getPostEntity(postId, title, content, 1L, "username", "password");
        Post post = Post.fromPostEntity(postEntity);

        when(postService.modify(eq(postId), eq(title), eq(content), any(String.class))).thenReturn(post);

        // expected
        mockMvc.perform(put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트수정_로그인하지않은_경우_실패() throws Exception {
        Long postId = 1L;
        String title = "test";
        String content = "test content";

        mockMvc.perform(put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트수정_작성자와_수정하려는_사람이_다른_경우_실패() throws Exception {
        Long postId = 1L;
        String title = "test";
        String content = "test content";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(postId), eq(title), eq(content), any());

        mockMvc.perform(put("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트수정_수정하려는_포스트가_존재하지_않는_경우_실패() throws Exception {
        Long postId = 1L;
        String title = "test";
        String content = "test content";

        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(postId), eq(title), eq(content), any());

        mockMvc.perform(put("/api/v1/post/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void 포스트삭제_성공() throws Exception {
        // given
        Long postId = 1L;


        // expected
        mockMvc.perform(delete("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트삭제_로그인하지않은_경우_실패() throws Exception {
        // given
        Long postId = 1L;

        // expected
        mockMvc.perform(delete("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트삭제_작성자와_삭제하려는_사람이_다른_경우_실패() throws Exception {
        // given
        Long postId = 1L;

        // mocking
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        // expected
        mockMvc.perform(delete("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트삭제_삭제하려는_포스트가_존재하지_않는_경우_실패() throws Exception {
        // given
        Long postId = 1L;

        // mocking
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        // expected
        mockMvc.perform(delete("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 피드목록_조회_성공() throws Exception {
        // given
        when(postService.getFeeds(any())).thenReturn(Page.empty());

        // expected
        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 피드목록_로그인하지않은_경우_실패() throws Exception {
        // given
        when(postService.getFeeds(any())).thenReturn(Page.empty());

        // expected
        mockMvc.perform(delete("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 나의포스트목록_조회_성공() throws Exception {
        // given
        when(postService.getMyPosts(any(), any())).thenReturn(Page.empty());

        // expected
        mockMvc.perform(get("/api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 나의포스트목록_로그인하지않은_경우_실패() throws Exception {
        // given
        when(postService.getMyPosts(any(), any())).thenReturn(Page.empty());

        // expected
        mockMvc.perform(delete("/api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
