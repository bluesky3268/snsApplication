package com.hyunbenny.snsApplication.controller.response;

import com.hyunbenny.snsApplication.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String body;
    private UserResponse user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                UserResponse.fromUser(post.getUser()),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
