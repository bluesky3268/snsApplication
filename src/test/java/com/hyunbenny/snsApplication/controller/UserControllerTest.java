package com.hyunbenny.snsApplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunbenny.snsApplication.controller.request.UserJoinRequest;
import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.model.User;
import com.hyunbenny.snsApplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        when(userService.join(username, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void 회원가입시_이미_username가_있는_경우_실패를_반환한다() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        when(userService.join(username, password)).thenThrow(new SnsApplicationException());

        mockMvc.perform(post("/api/v1/users/join")
                                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        when(userService.login(username, password)).thenReturn("test_access_token");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원가입이_안된_username으로_로그인을_시도하면_에러반환() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        when(userService.login(username, password)).thenThrow(new SnsApplicationException());

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 틀린_password로_로그인을_시도하면_에러반환() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        when(userService.login(username, password)).thenThrow(new SnsApplicationException());

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

}
