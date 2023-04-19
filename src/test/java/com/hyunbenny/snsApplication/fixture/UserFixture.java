package com.hyunbenny.snsApplication.fixture;

import com.hyunbenny.snsApplication.model.Roles;
import com.hyunbenny.snsApplication.model.entity.UserEntity;

public class UserFixture {
    public static UserEntity getUserEntity(Long userId, String username, String password) {
        UserEntity userEntity = UserEntity.of(userId, username, password);
        return userEntity;
    }
}
