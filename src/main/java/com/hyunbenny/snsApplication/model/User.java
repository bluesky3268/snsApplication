package com.hyunbenny.snsApplication.model;

import com.hyunbenny.snsApplication.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private Roles role;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public static User fromEntity(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
