package com.hyunbenny.snsApplication.fixture;

import com.hyunbenny.snsApplication.model.Roles;
import com.hyunbenny.snsApplication.model.entity.UserEntity;

public class UserFixture {
    public static UserEntity getUserEntity(String username, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIdForTest(1L);
        userEntity.setUsernameForTest(username);
        userEntity.changePassword(password);
        userEntity.createdAt();

        System.out.println("fixture : " + userEntity);

        return userEntity;
    }
}
