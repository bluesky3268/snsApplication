package com.hyunbenny.snsApplication.controller.response;

import com.hyunbenny.snsApplication.model.Roles;
import com.hyunbenny.snsApplication.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;

    private String username;

    private Roles role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
