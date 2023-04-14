package com.hyunbenny.snsApplication.fixture;

import com.hyunbenny.snsApplication.model.entity.UserEntity;

public class UserFixture {
    public static UserEntity getUserEntity(String username, String password) {

        return UserEntity.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();
    }
}
