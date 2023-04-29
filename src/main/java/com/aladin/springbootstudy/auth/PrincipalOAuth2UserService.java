package com.aladin.springbootstudy.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    /*
    *  구글로 부터 받은 userRequest 데이터에 대한 후처리 함수
    * 구글로그인 > 로그인 완료 > 코드리턴 > access token request
    * OAuth2 라이브러리가 loanUser 함수 호출 > 스코프 내의 회원 프로필 받아온다.
    * */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 스코프 회원 프로필 담고 있는 객체
        OAuth2User oAuth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }
}
