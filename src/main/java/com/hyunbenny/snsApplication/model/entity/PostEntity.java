package com.hyunbenny.snsApplication.model.entity;

import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"post\"")
@Getter
@SQLDelete(sql = "UPDATE \"post\" SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

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

    public PostEntity(String title, String content, UserEntity user) {
        this.title = title;
        this.content = content;
        this.user = user;
        createdAt();
    }

    public static PostEntity of(String title, String content, UserEntity user) {
        PostEntity entity = new PostEntity(title, content, user);
        return entity;
    }
}
