package com.hyunbenny.snsApplication.controller;

import com.hyunbenny.snsApplication.controller.request.UserJoinRequest;
import com.hyunbenny.snsApplication.controller.request.UserLoginRequest;
import com.hyunbenny.snsApplication.controller.response.AlarmResponse;
import com.hyunbenny.snsApplication.controller.response.Response;
import com.hyunbenny.snsApplication.controller.response.UserJoinResponse;
import com.hyunbenny.snsApplication.controller.response.UserLoginResponse;
import com.hyunbenny.snsApplication.model.Alarm;
import com.hyunbenny.snsApplication.model.User;
import com.hyunbenny.snsApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> getAlarmList(Pageable pageable, Authentication authentication) {
        return Response.success(userService.alarmList(authentication.getName(), pageable).map(alarm -> AlarmResponse.fromAlarm(alarm)));
    }


}
