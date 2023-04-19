package com.hyunbenny.snsApplication.controller.response;

import com.hyunbenny.snsApplication.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostModifyResponse {

    private Long id;

    private String title;

    private String content;

    private UserResponse user;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static PostModifyResponse fromPost(Post post) {
        return new PostModifyResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                UserResponse.fromUser(post.getUser()),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
                );
    }

}
