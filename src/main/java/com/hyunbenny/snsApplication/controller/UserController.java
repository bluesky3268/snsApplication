package com.hyunbenny.snsApplication.controller;

import com.hyunbenny.snsApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO : 회원가입 구현
    @PostMapping("/join")
    public void join(String username, String password) {
        userService.join(username, password);
    }

    // TODO : 로그인 구현
    @PostMapping("/login")
    public void login(String username, String password) {
        userService.login(username, password);
    }

}
