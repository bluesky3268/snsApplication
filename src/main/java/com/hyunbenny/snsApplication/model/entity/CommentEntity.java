package com.hyunbenny.snsApplication.model.entity;

import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "\"comment\"", indexes = {
        @Index(name = "comment_entity_post_id_idx", columnList = "post_id")
})
@Getter
@SQLDelete(sql = "UPDATE \"comment\" SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    public void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public CommentEntity() {}

    public CommentEntity(PostEntity post, String comment, UserEntity user) {
        this.post = post;
        this.comment = comment;
        this.user = user;
        createdAt();
    }

    public CommentEntity(Long id, PostEntity post, String comment, UserEntity user) {
        this.id = id;
        this.post = post;
        this.comment = comment;
        this.user = user;
        createdAt();
    }

    public static CommentEntity of(PostEntity post, String comment, UserEntity user) {
        return new CommentEntity(post, comment, user);
    }

    public static CommentEntity of(Long id, PostEntity post, String comment, UserEntity user) {
        return new CommentEntity(id, post, comment, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
