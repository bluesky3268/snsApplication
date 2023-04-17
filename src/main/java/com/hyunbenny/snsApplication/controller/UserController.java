package com.hyunbenny.snsApplication.controller;

import com.hyunbenny.snsApplication.controller.request.UserJoinRequest;
import com.hyunbenny.snsApplication.controller.response.Response;
import com.hyunbenny.snsApplication.controller.response.UserJoinResponse;
import com.hyunbenny.snsApplication.model.User;
import com.hyunbenny.snsApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO : 회원가입 구현
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUsername(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    // TODO : 로그인 구현
    @PostMapping("/login")
    public void login(String username, String password) {
        userService.login(username, password);
    }

}
