package com.hyunbenny.snsApplication.fixture;

import com.hyunbenny.snsApplication.model.entity.PostEntity;
import com.hyunbenny.snsApplication.model.entity.UserEntity;

public class PostFixture {
    public static PostEntity getPostEntity(Long postId, String title, String content, Long userId, String username, String password) {
        UserEntity user = UserEntity.of(userId, username, password);

        return PostEntity.of(postId, title, content, user);
    }
}
