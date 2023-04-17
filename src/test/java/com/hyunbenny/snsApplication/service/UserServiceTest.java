package com.hyunbenny.snsApplication.service;

import com.hyunbenny.snsApplication.exception.SnsApplicationException;
import com.hyunbenny.snsApplication.fixture.UserFixture;

import com.hyunbenny.snsApplication.model.entity.UserEntity;
import com.hyunbenny.snsApplication.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void 회원가입이_정상적으로_동작() {
        String username = "testUser";
        String password = "testPassword";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encrypted_password");
        when(userEntityRepository.save(any())).thenReturn(UserFixture.getUserEntity(username, password));

        Assertions.assertDoesNotThrow(() -> userService.join(username, password));
    }

    @Test
    void 회원가입시_해당_username이_이미_존재하는_경우() {
        String username = "testUser";
        String password = "testPassword";
        UserEntity fixture = UserFixture.getUserEntity(username, password);


        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encrypted_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(username, password));
    }

    @Test
    void 로그인이_정상적으로_동작() {
        String username = "testUser";
        String password = "testPassword";

        UserEntity fixture = UserFixture.getUserEntity(username, password);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(() -> userService.login(username, password));
    }

    @Test
    void 로그인시_해당_username이_없는_경우() {
        String username = "testUser";
        String password = "testPassword";

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(username, password));
    }

    @Test
    void 로그인시_password가_틀린_경우() {
        String username = "testUser";
        String password = "testPassword";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserFixture.getUserEntity(username, password);

        when(userEntityRepository.findByUsername(username)).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(username, wrongPassword));
    }




}
