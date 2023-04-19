package com.hyunbenny.snsApplication.controller;

import com.hyunbenny.snsApplication.controller.request.PostCreateRequest;
import com.hyunbenny.snsApplication.controller.request.PostModifyRequest;
import com.hyunbenny.snsApplication.controller.response.PostModifyResponse;
import com.hyunbenny.snsApplication.controller.response.Response;
import com.hyunbenny.snsApplication.model.Post;
import com.hyunbenny.snsApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> createPost(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getContent(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostModifyResponse> modifyPost(@PathVariable Long postId,
                                     @RequestBody PostModifyRequest request,
                                     Authentication authentication) {
        Post post = postService.modify(postId, request.getTitle(), request.getContent(), authentication.getName());
        log.info("post : {}", post);
        return Response.success(PostModifyResponse.fromPost(post));
    }
}
