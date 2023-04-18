package com.hyunbenny.snsApplication.controller;

import com.hyunbenny.snsApplication.controller.request.PostCreateRequest;
import com.hyunbenny.snsApplication.controller.response.Response;
import com.hyunbenny.snsApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> createPost(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getContent(), authentication.getName());
        return Response.success();
    }
}
