package com.hyunbenny.snsApplication.controller;

import com.hyunbenny.snsApplication.controller.request.UserJoinRequest;
import com.hyunbenny.snsApplication.controller.request.UserLoginRequest;
import com.hyunbenny.snsApplication.controller.response.AlarmResponse;
import com.hyunbenny.snsApplication.controller.response.Response;
import com.hyunbenny.snsApplication.controller.response.UserJoinResponse;
import com.hyunbenny.snsApplication.controller.response.UserLoginResponse;
import com.hyunbenny.snsApplication.exception.ErrorCode;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.Alarm;
import com.hyunbenny.snsApplication.model.User;
import com.hyunbenny.snsApplication.service.AlarmService;
import com.hyunbenny.snsApplication.service.UserService;
import com.hyunbenny.snsApplication.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AlarmService alarmService;

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
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "cast to User class failed"));

        return Response.success(userService.alarmList(user.getId(), pageable).map(alarm -> AlarmResponse.fromAlarm(alarm)));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        // Front단에서 EventSource에서 header세팅을 지원하지 않기  때문에 토큰을 파라미터로 넘김
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "cast to User class failed"));

        return alarmService.connectAlarm(user.getId());
    }


}
