package com.aladin.springbootstudy.auth;

import com.aladin.springbootstudy.entity.KakaoProfileEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/*
 * 로그인 진행이 완료되면 Security Session 생성
 * Security ContextHolder
 * 이 컨텍스트 홀더에 세션 객체에 들어갈 수 있는 오브젝트가 정해져 있음.(Authentication 객체)
 * Authentication 객체 안에 User 정보가 있어야 됨.
 * User Object 타입 > UserDetails 타입 객체
 *
 * Security Session => Authentication => UserDetails(PrincipalDetails)
 * PrincipalDetails가 UserDetails를 구현했으므로 타입이 같다.
 * 그렇기 때문에 UserDetails 타입의 객체만 담고 있는 Authentication 객체에
 * 주입할 수 있다.
 */
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Map<String, Object> attributes;
    private KakaoProfileEntity kakaoProfileEntity;

    //카카오
    public PrincipalDetails(KakaoProfileEntity kakaoProfileEntity) {
        this.kakaoProfileEntity = kakaoProfileEntity;
    }

    public PrincipalDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }



    /*
    *  해당 User의 권한을 리턴하는 메소드
    * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return kakaoProfileEntity.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getName() {
        return (String) attributes.get("sub");
    }

    @Override
    public boolean isEnabled() {
        /*
        *  휴면 계정 등을 제어하는 메소드
        *  현재시간 - user로그인 시간 등으로 어플리케이션에 맞게 설정
        * */
        return true;
    }
}
