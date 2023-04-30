package com.aladin.springbootstudy.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    PrincipalOAuth2UserService principalOAuthUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf();
        http.authorizeRequests()
                .antMatchers("/api/v1/get-api/login").permitAll()
                //.antMatchers("/accounts/**").authenticated()
                .antMatchers("/info/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGE')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .and()
                .oauth2Login()
                .loginPage("/api/v1/get-api/login")
                .loginProcessingUrl("/accounts/info")
                /***
                 * 로그인 후 후처리
                 * 1. 로그인 후 access token(권한) 코드 받기(인증)
                 * 2. 사용자 정보 접근 가능한 권한 인가
                 * 3. 사용자 프로필 정보를 가져오기
                 * 4. 그 정보를 토대로 회원가입 자동 진행
                 * 5. * OAuth2 라이브러리 사용 시
                 *    구글로그인 완료되면 (엑세스토큰 + 사용자 프로필 정보) 바로 받는다.
                 */
                .userInfoEndpoint()
                .userService(principalOAuthUserService)


        ;

        http.logout()
                .logoutUrl("/logout")   // 로그아웃 처리 URL (= form action url)
                //.logoutSuccessUrl("/login") // 로그아웃 성공 후 targetUrl,
                // logoutSuccessHandler 가 있다면 효과 없으므로 주석처리.
                .addLogoutHandler((request, response, authentication) -> {
                    // 사실 굳이 내가 세션 무효화하지 않아도 됨.
                    // LogoutFilter가 내부적으로 해줌.
                    HttpSession session = request.getSession();
                    if (session != null) {
                        session.invalidate();
                    }
                })  // 로그아웃 핸들러 추가
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/login");
                }) // 로그아웃 성공 핸들러
                .deleteCookies("remember-me"); // 로그아웃 후 삭제할 쿠키 지정


    }
}
