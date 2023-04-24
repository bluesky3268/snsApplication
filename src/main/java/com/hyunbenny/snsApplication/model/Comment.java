package com.hyunbenny.snsApplication.model;

import com.hyunbenny.snsApplication.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Comment {

    private Long id;

    private Long postId;

    private String comment;

    private String username;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Comment fromCommentEntity(CommentEntity entity) {
        return new Comment(
                entity.getId(),
                entity.getPost().getId(),
                entity.getComment(),
                entity.getUser().getUsername(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
                );
    }

}
