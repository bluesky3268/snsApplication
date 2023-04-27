package com.hyunbenny.snsApplication.configuration;

import com.hyunbenny.snsApplication.configuration.filter.JwtTokenFilter;
import com.hyunbenny.snsApplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Value("${jwt.token.secretKey}")
    private String key;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // /api/ 로 시작하는 요청들만 통과를 시키고 아닌 경우는 무시한다.
        web.ignoring().regexMatchers("^(?!/api/).*")
                .antMatchers("/api/*/users/join", "/api/*/users/login"); // 토큰 필터 적용을 제외하기 위해서 아래에 있던 걸 여기로 옮김(라인을 지우지 않고 주석처리 함)
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
//                .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 예외가 발생하면 지정한 엔트리 포인드로 이동시키기
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class);
    }
}
