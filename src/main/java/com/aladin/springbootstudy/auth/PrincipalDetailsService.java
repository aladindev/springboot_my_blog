package com.aladin.springbootstudy.auth;

import com.aladin.springbootstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
* Security Config에서 설정한
* loginProcessingUrl("/accounts/info")
* login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는
* loadUserByUsername 함수가 실행된다. (Spring Security 구성임)
* */
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
