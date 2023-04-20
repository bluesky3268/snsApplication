package com.hyunbenny.snsApplication.controller;

import com.hyunbenny.snsApplication.controller.request.PostCreateRequest;
import com.hyunbenny.snsApplication.controller.request.PostModifyRequest;
import com.hyunbenny.snsApplication.controller.response.PostModifyResponse;
import com.hyunbenny.snsApplication.controller.response.PostResponse;
import com.hyunbenny.snsApplication.controller.response.Response;
import com.hyunbenny.snsApplication.model.Post;
import com.hyunbenny.snsApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public Response<Page<PostResponse>> getFeeds(Pageable pageable, Authentication authentication) {
        Page<Post> feeds = postService.getFeeds(pageable);
        return Response.success(feeds.map(post -> PostResponse.fromPost(post)));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> getMyPosts(Pageable pageable, Authentication authentication) {
        Page<Post> myPosts = postService.getMyPosts(authentication.getName(), pageable);
        return Response.success(myPosts.map(post -> PostResponse.fromPost(post)));
    }

    @PostMapping
    public Response<Void> createPost(@RequestBody PostCreateRequest request,
                                     Authentication authentication) {
        postService.create(request.getTitle(), request.getContent(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostModifyResponse> modifyPost(@PathVariable Long postId,
                                     @RequestBody PostModifyRequest request,
                                     Authentication authentication) {
        Post post = postService.modify(postId, request.getTitle(), request.getContent(), authentication.getName());
        return Response.success(PostModifyResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> modifyPost(@PathVariable Long postId,
                                    Authentication authentication) {
        postService.delete(postId, authentication.getName());
        return Response.success();
    }

}
