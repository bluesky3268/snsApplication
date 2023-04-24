package com.hyunbenny.snsApplication.controller.response;

import com.hyunbenny.snsApplication.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long postId;
    private String comment;
    private String username;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static CommentResponse fromPost(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getComment(),
                comment.getUsername(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

}
