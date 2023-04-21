package com.hyunbenny.snsApplication.model.entity;

import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "\"like\"")
@Getter
@SQLDelete(sql = "UPDATE \"like\" SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_io")
    private PostEntity post;

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

    public LikeEntity() {}

    public LikeEntity(PostEntity post, UserEntity user) {
        this.post = post;
        this.user = user;
        createdAt();
    }

    public LikeEntity(Long id, PostEntity post, UserEntity user) {
        this.id = id;
        this.post = post;
        this.user = user;
        createdAt();
    }

    public static LikeEntity of(PostEntity post, UserEntity user) {
        return new LikeEntity(post, user);
    }

    public static LikeEntity of(Long id, PostEntity post, UserEntity user) {
        return new LikeEntity(id, post, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeEntity that = (LikeEntity) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
